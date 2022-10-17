package com.epam.cash.register.command;

import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.util.RequestUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReceiptFilterCommand implements Command {

    private static final Logger log = LogManager.getLogger(ReceiptFilterCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        List<Receipt> receipts = (List<Receipt>) req.getAttribute("receipts");

        // if request is empty, this method will be skipped
        // because of there is nothing to filter
        if (receipts == null) {
            return;
        }

        String[] tempArr = req.getParameterMap().get("filtering");

        // if request hasn`t a field "filtering",
        // this method will be skipped because of
        // it isn`t filtering
        if (tempArr == null || !tempArr[0].equals("true")) {
            log.trace("filtering hasnt worked!");
            return;
        }

        Map<String, String> params = req.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        stringEntry -> stringEntry.getValue()[0])
                );


        String tempValue = null;

        final String creatorName = (tempValue = params.get("creatorName")) != null ? tempValue : null;
        final String receiptCode = (tempValue = params.get("receiptCode")) != null ? tempValue : null;
        final String cancelerName = (tempValue = params.get("cancelerName")) != null ? tempValue : null;

        final Double minPrice = (tempValue = params.get("minPrice")) != null ? Double.parseDouble(tempValue) : null;
        final Double maxPrice = (tempValue = params.get("maxPrice")) != null ? Double.parseDouble(tempValue) : null;

        final Boolean isDone = (tempValue = params.get("isDone")) != null ? Boolean.parseBoolean(tempValue) : null;
        final Boolean isCanceled = (tempValue = params.get("isCanceled")) != null ? Boolean.parseBoolean(tempValue) : null;

        List<Receipt> filteredReceipts = receipts.stream()
                .filter(receipt -> creatorName == null || receipt.getUserCreator().getUsername().equals(creatorName))
                .filter(receipt -> receiptCode == null || receipt.getReceiptCode().equals(receiptCode))
                .filter(receipt -> cancelerName == null ||
                        (receipt.getUserCanceler() != null && receipt.getUserCanceler().getUsername().equals(cancelerName)))
                .filter(receipt -> (minPrice == null || minPrice <= receipt.getTotalPrice())
                        && (maxPrice == null || receipt.getTotalPrice() <= maxPrice))
                .filter(receipt -> isDone == null || receipt.isDone() == isDone)
                .filter(receipt -> isCanceled == null || receipt.isCanceled() == isCanceled)
                .collect(Collectors.toList());

        log.trace("The size of filtered list is " + filteredReceipts.size());
        req.setAttribute("receipts", filteredReceipts);
    }
}
