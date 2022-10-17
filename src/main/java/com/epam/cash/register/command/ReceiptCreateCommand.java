package com.epam.cash.register.command;

import com.epam.cash.register.entity.ItemReceipt;
import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.service.ProductService;
import com.epam.cash.register.service.ProductServiceImpl;
import com.epam.cash.register.service.ReceiptService;
import com.epam.cash.register.service.ReceiptServiceImpl;
import com.epam.cash.register.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class ReceiptCreateCommand implements Command {

    private final ReceiptService receiptService = new ReceiptServiceImpl();
    private final ProductService productService = new ProductServiceImpl();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> map = RequestUtil.getMapFromBody(req.getInputStream());

        String receiptCode = (String) req.getSession().getAttribute("receiptCode");

        User user = (User) req.getSession().getAttribute("user");


        if (map.containsKey("done")) {
            Receipt receipt = receiptService.findByCode(map.get("receiptCode"));
            receiptService.acceptReceipt(receipt);

            String currentReceiptCode = (String) req.getSession().getAttribute("receiptCode");
            if (currentReceiptCode != null && currentReceiptCode.equals(map.get("receiptCode"))) {
                req.getSession().setAttribute("receiptCode", null);
            }

            return;
        }

        if (receiptCode == null) {
            receiptCode = UUID.randomUUID().toString();
            req.getSession().setAttribute("receiptCode", receiptCode);
            Receipt receipt = new Receipt(
                    receiptCode,
                    user,
                    new ArrayList<>()
            );
            receipt.addItemReceipt(
                    new ItemReceipt(
                            0L,
                            Long.parseLong(map.get("quantityProduct")),
                            productService.findById(Long.parseLong(map.get("productID")))
                    )
            );
            receiptService.insert(receipt);
            return;
        }
        Receipt receipt = receiptService.findByCode(receiptCode);
        receipt.addItemReceipt(
                new ItemReceipt(
                        0L,
                        Long.parseLong(map.get("quantityProduct")),
                        productService.findById(Long.parseLong(map.get("productID")))
                )
        );
        receiptService.update(receipt);
    }
}
