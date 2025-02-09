package com.anamnesys.service;

import com.anamnesys.controller.dto.AnswerResponseDTO;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.borders.SolidBorder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

    private static final DeviceRgb HEADER_BACKGROUND = new DeviceRgb(63, 81, 181);
    private static final DeviceRgb SECTION_BACKGROUND = new DeviceRgb(237, 242, 247);

    public byte[] generateRecordPdf(AnswerResponseDTO answerBD) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Cabeçalho
            Paragraph title = new Paragraph(answerBD.getNameRecord())
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(HEADER_BACKGROUND)
                    .setFontColor(ColorConstants.WHITE)
                    .setPadding(10);
            document.add(title);

            // Informações do paciente
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
                    .useAllAvailableWidth()
                    .setMarginTop(20);

            addInfoRow(infoTable, "Nome do Paciente:", answerBD.getName());
            addInfoRow(infoTable, "Telefone:", answerBD.getPhone());
            addInfoRow(infoTable, "E-mail:", answerBD.getEmail());
            addInfoRow(infoTable, "Data de preenchimento:",
                    answerBD.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

            document.add(infoTable);
            document.add(new Paragraph("\n").setFontSize(10));

            // Parse e agrupamento das perguntas por seção
            ObjectMapper mapper = new ObjectMapper();
            JsonNode answersNode = mapper.readTree(answerBD.getAnswer());
            JsonNode questionsAndAnswers = answersNode.get("perguntasComRespostas");

            Map<String, List<JsonNode>> sectionedQuestions = new HashMap<>();
            StringBuilder termsText = new StringBuilder();

            if (questionsAndAnswers != null && questionsAndAnswers.isArray()) {
                for (JsonNode qa : questionsAndAnswers) {
                    String type = qa.has("tipo") ? qa.get("tipo").asText() : "";

                    if ("TERM".equalsIgnoreCase(type)) {
                        termsText.append(qa.get("texto").asText()).append("\n");
                        continue;
                    }

                    String sectionKey = qa.has("section") ? qa.get("section").asText() : "outros";
                    String sectionDescription = qa.has("descriptionSection") ?
                            qa.get("descriptionSection").asText() : "Outras Informações";

                    sectionedQuestions
                            .computeIfAbsent(sectionKey, k -> new ArrayList<>())
                            .add(qa);
                }

                // Adicionar cada seção
                sectionedQuestions.forEach((sectionKey, questions) -> {
                    if (!questions.isEmpty()) {
                        String sectionDescription = questions.getFirst().get("descriptionSection").asText();

                        // Adicionar cabeçalho da seção
                        Paragraph sectionHeader = new Paragraph(sectionDescription)
                                .setFontSize(14)
                                .setBold()
                                .setBackgroundColor(SECTION_BACKGROUND)
                                .setPadding(5)
                                .setMarginTop(15);
                        document.add(sectionHeader);

                        // Criar tabela para as perguntas da seção
                        Table qaTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                                .useAllAvailableWidth()
                                .setMarginTop(5);

                        questions.forEach(qa -> {
                            String question = qa.has("texto") ? qa.get("texto").asText() : "";
                            String answer = qa.has("resposta") && !qa.get("resposta").isNull() ?
                                    qa.get("resposta").asText() : "Não informado";
                            String type = qa.has("tipo") ? qa.get("tipo").asText() : "";

                            Cell questionCell = createCell(question, true)
                                    .setBackgroundColor(ColorConstants.WHITE);
                            Cell answerCell = createCell(formatAnswer(answer, type), false)
                                    .setBackgroundColor(ColorConstants.WHITE);

                            qaTable.addCell(questionCell);
                            qaTable.addCell(answerCell);
                        });

                        document.add(qaTable);
                    }
                });

                // Adicionar termos se existirem
                if (!termsText.isEmpty()) {
                    document.add(new Paragraph("\n").setFontSize(12));

                    Paragraph termsTitle = new Paragraph("Termo de compromisso e responsabilidade")
                            .setFontSize(14)
                            .setBold()
                            .setBackgroundColor(SECTION_BACKGROUND)
                            .setPadding(5);
                    document.add(termsTitle);

                    Paragraph termsParagraph = new Paragraph(termsText.toString())
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.JUSTIFIED)
                            .setMarginTop(10);
                    document.add(termsParagraph);

                    document.add(new Paragraph("\n").setFontSize(6));
                    Paragraph checkbox = new Paragraph("☑ Eu concordo com os termos acima.")
                            .setFontSize(10)
                            .setBold()
                            .setMarginTop(10);
                    document.add(checkbox);

                    document.add(new Paragraph("\n").setFontSize(10));
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
            }

            // Rodapé
            document.add(new Paragraph("\n").setFontSize(10));
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
        Cell labelCell = createCell(label, true)
                .setBackgroundColor(SECTION_BACKGROUND);
        Cell valueCell = createCell(value != null ? value : "Não informado", false)
                .setBackgroundColor(ColorConstants.WHITE);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private Cell createCell(String text, boolean isBold) {
        Paragraph p = new Paragraph(text != null ? text : "")
                .setFontSize(10);
        if (isBold) {
            p.setBold();
        }
        return new Cell()
                .add(p)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(5)
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1));
    }

    private String formatAnswer(String answer, String type) {
        if (answer == null || answer.isEmpty()) {
            return "Não informado";
        }

        switch (type.toUpperCase()) {
            case "BOOLEAN":
                return "Sim".equalsIgnoreCase(answer) ? "✓ Sim" : "✗ Não";
            case "DATE":
                try {
                    LocalDateTime date = LocalDateTime.parse(answer);
                    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception e) {
                    return answer;
                }
            default:
                return answer;
        }
    }
}

