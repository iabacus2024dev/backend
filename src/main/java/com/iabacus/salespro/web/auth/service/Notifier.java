package com.iabacus.salespro.web.auth.service;

public interface Notifier {

    void send(String to, String subject, String message);

}
