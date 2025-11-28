package com.example.controller;

import com.example.model.Post;
import com.example.repository.PostRepository;
import com.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Base64;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PostRepository postRepository;

    // 1. Hiển thị trang Xác nhận (Xử lý 2 trường hợp: FULL hoặc DEPOSIT)
    @GetMapping("/checkout")
    public String showCheckoutPage(@RequestParam("postId") Integer postId,
                                   @RequestParam(value = "type", defaultValue = "DEPOSIT") String type, // Nhận loại thanh toán
                                   Model model) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            if ("SOLD".equals(post.getStatus())) return "redirect:/posts";

            model.addAttribute("post", post);

            BigDecimal price = (post.getPrice() == null) ? BigDecimal.ZERO : post.getPrice();
            BigDecimal amountToPay;

            // LOGIC TÍNH TIỀN
            if ("FULL".equals(type)) {
                // Nếu chọn Mua luôn -> Trả 100%
                amountToPay = price;
            } else {
                // Nếu chọn Đặt cọc -> Trả 10%
                amountToPay = price.multiply(new BigDecimal("0.1"));
            }

            model.addAttribute("totalPrice", price);      // Giá gốc
            model.addAttribute("amountToPay", amountToPay); // Số tiền cần trả lúc này
            model.addAttribute("paymentType", type);      // Gửi loại thanh toán sang view để hiển thị chữ

            return "checkout";
        }
        return "redirect:/posts";
    }

    // 2. Xử lý Thanh toán
    @PostMapping("/pay")
    public String payToVNPAY(@RequestParam("amount") BigDecimal amount,
                             @RequestParam("postId") Integer postId,
                             Model model) {

        long finalAmount = amount.longValue();
        if (finalAmount < 5000) finalAmount = 10000;

        String paymentUrl = paymentService.createVnPayUrl(finalAmount, postId);

        try {
            String qrCodeBase64 = generateQRCodeImage(paymentUrl, 300, 300);
            model.addAttribute("qrCode", qrCodeBase64);
            model.addAttribute("paymentUrl", paymentUrl);
            model.addAttribute("amount", finalAmount);
            return "payment_qr";
        } catch (Exception e) {
            return "redirect:" + paymentUrl;
        }
    }

    // 3. Nhận kết quả
    @GetMapping("/vnpay-return")
    public String returnUrl(@RequestParam(value = "vnp_ResponseCode") String responseCode,
                            @RequestParam(value = "vnp_OrderInfo") String orderInfo) {
        if ("00".equals(responseCode)) {
            try {
                String[] parts = orderInfo.split("_");
                int postId = Integer.parseInt(parts[0]);
                Post post = postRepository.findById(postId).orElse(null);
                if (post != null) {
                    post.setStatus("SOLD");
                    postRepository.save(post);
                }
            } catch (Exception e) { e.printStackTrace(); }
            return "transaction_success";
        } else {
            return "transaction_failed";
        }
    }

    private String generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(pngData);
    }

    @GetMapping("")
    public String showTransactionPage() { return "transaction"; }
}