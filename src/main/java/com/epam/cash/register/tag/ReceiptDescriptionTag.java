package com.epam.cash.register.tag;

import com.epam.cash.register.entity.Receipt;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.IterationTag;
import java.io.IOException;


public class ReceiptDescriptionTag extends BodyTagSupport {

    private Receipt receipt;

    @Override
    public int doStartTag() throws JspException {
        JspWriter writer = pageContext.getOut();
        try {
            writer.print(String.format("<td scope=\"row\">%d</td>", receipt.getId()));
            writer.print(String.format("<td>%s</td>", receipt.getReceiptCode()));
            writer.print(String.format("<td>%.2f</td>", receipt.getTotalPrice()));
            writer.print(String.format("<td>%s</td>", receipt.getUserCreator().getUsername()));
            writer.print(String.format("<td>%b</td>", receipt.isDone()));
            writer.print(String.format("<td>%b</td>", receipt.isCanceled()));
            writer.print(String.format("<td>%s</td>", receipt.getUserCanceler() != null ?
                    receipt.getUserCanceler().getUsername() : "Not Found"));
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
}
