package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.exception.InternalServerHandling;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    private DataSource dataSource;

    // GET CONNECTION TO DATABASE
    private Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new InternalServerHandling(e.getMessage());
        }
    }
    @Override
    public JasperPrint generateInvoice(Integer bookingId) throws Exception {
        InputStream fileReport = new ClassPathResource("reports/invoice_aeroswift.jasper").getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fileReport);
        Map<String, Object> param = new HashMap<>();
        param.put("BOOKING_ID", bookingId);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, getConnection()); // param : BOOKING_ID
        return jasperPrint;
    }

}
