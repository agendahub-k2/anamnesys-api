package com.anamnesys.controller;

import com.anamnesys.controller.dto.AnswerResponseDTO;
import com.anamnesys.exception.AnswerNotFoundException;
import com.anamnesys.repository.AnswerRepository;
import com.anamnesys.repository.model.AnswerModel;
import com.anamnesys.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("answer")
public class AnswerController {

    @Autowired
    private AnswerRepository repository;
    @Autowired
    private final PdfService pdfService;
    private static final Logger logger = LoggerFactory.getLogger(AnswerController.class);

    public AnswerController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/{linkId}")
    public ResponseEntity<AnswerModel> getAnswerById(@PathVariable String linkId) {

        logger.info("Received request get answer by id {}", linkId);
        AnswerModel answer = repository.findById(linkId).orElseThrow(AnswerNotFoundException::new);

        logger.info("process successfully get answer by id {}", linkId);
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/{linkId}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String linkId, HttpServletResponse response) {
        AnswerResponseDTO answer = repository.findAnswerDetailsById(linkId)
                .orElseThrow(AnswerNotFoundException::new);
        try {
            byte[] pdfContent = pdfService.generateRecordPdf(
                    answer
            );

            String filename = answer.getName() + "_" + answer.getNameRecord() + ".pdf";

            // Expondo o cabeçalho Content-Disposition
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfContent);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
