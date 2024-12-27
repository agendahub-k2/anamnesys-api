package com.anamnesys.controller.dto;

import com.anamnesys.repository.model.QuestionModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class QuestionRequest {

    private Long id; // Não validado porque pode ser opcional, por exemplo, em um update.

    @NotBlank(message = "Pergunta não pode ser vazia ou nula.")
    private String question;

    @NotNull(message = "Número da Seção não pode ser nulo.")
    private Long section;

    @NotBlank(message = "Descrição da seção não pode ser vazia ou nula.")
    private String descriptionSection;

    @NotNull(message = "Precisa informar se a pergunta é obrigatória ou não.")
    private Boolean isRequired;

    @NotNull(message = "O tipo da pergunta não pode ser nulo.")
    private QuestionModel.QuestionType questionType;

    private List<String> options;
}

