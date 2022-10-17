package com.epam.cash.register.command;

import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.service.ReceiptService;
import com.epam.cash.register.service.ReceiptServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductShowerElementReceiptCommand implements Command {

    private final ReceiptService receiptService = new ReceiptServiceImpl();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String receiptCode = (String) req.getSession().getAttribute("receiptCode");
        Map<Long, Long> mapProductAtReceipt = new HashMap<>();

        if (receiptCode != null) {
            Receipt receipt = receiptService.findByCode(receiptCode);
            receipt.getItems().forEach(
                    itemReceipt -> mapProductAtReceipt.put(itemReceipt.getProduct().getId(), itemReceipt.getQuantity())
            );

        }
        req.setAttribute("mapProductAtReceipt", mapProductAtReceipt);
    }
}
