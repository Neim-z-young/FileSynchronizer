package com.oyoungy.servlet;

import com.oyoungy.util.SyncFileInfoForm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Logger;

public class FileSyncer extends HttpServlet {
    Logger logger = Logger.getLogger(FileSyncer.class.getName());

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, java.io.IOException {

    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, java.io.IOException {
        SyncFileInfoForm sFIF = null;
        try {
            sFIF = SyncFileInfoForm.parseHttpRequest(request);
        } catch (IllegalAccessException e) {
            logger.warning("wrong request body");
            e.printStackTrace();
        }
        if(sFIF!=null && sFIF.getSourceFile()!=null){

        }
    }
}
