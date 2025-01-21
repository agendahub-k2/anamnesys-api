package com.anamnesys.service;

import com.anamnesys.controller.dto.AnswerResponseDTO;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generateRecordPdf(AnswerResponseDTO answerBD) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Adiciona título
            Paragraph title = new Paragraph(answerBD.getNameRecord())
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Adiciona informações do paciente
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{30, 70})).useAllAvailableWidth();
            infoTable.setMarginTop(10);

            addInfoRow(infoTable, "Nome:", answerBD.getName());
            addInfoRow(infoTable, "Telefone:", answerBD.getPhone());
            addInfoRow(infoTable, "E-mail:", answerBD.getEmail());
            addInfoRow(infoTable, "Data de preenchimento:", answerBD.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

            document.add(infoTable);

            // Adiciona espaço antes da tabela de perguntas e respostas
            document.add(new Paragraph("\n").setFontSize(6));

            // Parse do JSON de respostas
            ObjectMapper mapper = new ObjectMapper();
            JsonNode answersNode = mapper.readTree(answerBD.getAnswer());
            JsonNode questionsAndAnswers = answersNode.get("perguntasComRespostas");

            // Cria tabela para as respostas
            Table qaTable = new Table(UnitValue.createPercentArray(new float[]{40, 60})).useAllAvailableWidth();

            // Adiciona cada pergunta e resposta na tabela
            for (JsonNode qa : questionsAndAnswers) {
                String type = qa.get("tipo").asText();
                if ("TERM".equalsIgnoreCase(type)) {
                    continue;
                }
                String question = qa.get("texto").asText();
                String answer = qa.has("resposta") && !qa.get("resposta").isNull()
                        ? qa.get("resposta").asText()
                        : "Não informado";

                qaTable.addCell(createCell(question, false));
                qaTable.addCell(createCell(answer, true));
            }

            document.add(qaTable);

            // Adiciona rodapé
            document.add(new Paragraph("\n").setFontSize(6));
            Paragraph footer = new Paragraph("Documento gerado por " + answerBD.getNameUser() +" em "+
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(footer);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private void addInfoRow(Table table, String label, String value) {
        table.addCell(createCell(label, true).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell(createCell(value, false));
    }

    private Cell createCell(String text, boolean isBold) {
        Paragraph p = new Paragraph(text).setFontSize(10);
        if (isBold) {
            p.setBold();
        }
        return new Cell().add(p)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }
}

