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
            JsonNode questionsAndAnswers = answersNode != null ? answersNode.get("perguntasComRespostas") : null;

            if (questionsAndAnswers != null && questionsAndAnswers.isArray()) {
                // Cria tabela para as respostas
                Table qaTable = new Table(UnitValue.createPercentArray(new float[]{40, 60})).useAllAvailableWidth();

                StringBuilder termsText = new StringBuilder(); // Para armazenar os termos encontrados

                // Adiciona cada pergunta e resposta na tabela
                for (JsonNode qa : questionsAndAnswers) {
                    String type = qa.has("tipo") ? qa.get("tipo").asText() : "OUTRO";
                    String question = qa.has("texto") ? qa.get("texto").asText() : "Pergunta não especificada";
                    String answer = qa.has("resposta") && !qa.get("resposta").isNull()
                            ? qa.get("resposta").asText()
                            : "Não informado";

                    if ("TERM".equalsIgnoreCase(type)) {
                        termsText.append(question).append("\n");
                        continue; // Ignora termos na tabela principal
                    }

                    qaTable.addCell(createCell(question, false));
                    qaTable.addCell(createCell(answer, true));
                }

                document.add(qaTable);

                if (!termsText.isEmpty()) {
                    document.add(new Paragraph("\n").setFontSize(12));

                    // Adiciona o título
                    Paragraph termsTitle = new Paragraph("Termo de compromisso e responsabilidade")
                            .setFontSize(14)
                            .setBold();
                    document.add(termsTitle);

                    // Adiciona o texto dos termos
                    Paragraph termsParagraph = new Paragraph(termsText.toString())
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.JUSTIFIED);
                    document.add(termsParagraph);

                    // Adiciona o checkbox marcado e a frase de concordância
                    document.add(new Paragraph("\n").setFontSize(6)); // Espaço
                    Paragraph checkbox = new Paragraph("☑ Eu concordo com os termos acima.")
                            .setFontSize(10)
                            .setBold()
                            .setMarginTop(10);
                    document.add(checkbox);

                    // Adiciona a assinatura (nome do usuário)
                    document.add(new Paragraph("\n").setFontSize(6));
                    Paragraph signatureLabel = new Paragraph("Aceito por:").setFontSize(10).setBold();
                    Paragraph signature = new Paragraph(answerBD.getName())
                            .setFontSize(12)
                            .setItalic()
                            .setTextAlignment(TextAlignment.LEFT)
                            .setMarginTop(5)
                            .setUnderline();
                    document.add(signatureLabel);
                    document.add(signature);
                }

            } else {
                document.add(new Paragraph("Nenhuma pergunta ou resposta disponível.").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            }

            // Adiciona rodapé
            Paragraph footer = new Paragraph("Documento gerado por " + answerBD.getNameUser() + " em " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20);
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
