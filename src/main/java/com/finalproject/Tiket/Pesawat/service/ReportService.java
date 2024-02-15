package com.finalproject.Tiket.Pesawat.service;

import net.sf.jasperreports.engine.JasperPrint;

import java.io.InputStream;

public interface ReportService {
    JasperPrint generateInvoice(Integer bookingId) throws Exception;
}
