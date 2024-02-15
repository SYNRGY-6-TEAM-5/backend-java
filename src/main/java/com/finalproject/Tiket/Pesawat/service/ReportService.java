package com.finalproject.Tiket.Pesawat.service;

import net.sf.jasperreports.engine.JasperPrint;

public interface ReportService {
    JasperPrint generateInvoice(Integer bookingId) throws Exception;
}
