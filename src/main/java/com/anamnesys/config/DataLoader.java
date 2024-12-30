package com.anamnesys.config;

import com.anamnesys.repository.QuestionRepository;
import com.anamnesys.repository.SegmentRepository;
import com.anamnesys.repository.TemplateRepository;
import com.anamnesys.repository.model.QuestionModel;
import com.anamnesys.repository.model.SegmentModel;
import com.anamnesys.repository.model.TemplateModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(SegmentRepository segmentRepository,
                                      TemplateRepository templateRepository,
                                      QuestionRepository questionRepository) {

        // Criação do segmento
        SegmentModel saude = new SegmentModel(null, "Saúde", "Foco em histórico médico e alergias.");

        // Criação do template
        TemplateModel fichaMedicaCompleta = new TemplateModel(
                null,
                "Ficha de Anamnese Médica",
                saude,
                "Coleta detalhada de informações médicas para uso geral em saúde.",
                null
        );

        return args -> {
            // Verifica se o segmento já existe, caso contrário, salva
            if (segmentRepository.count() == 0) {
                segmentRepository.save(saude);
            } else {
                System.out.println("SegmentModel - Dados já estão no banco. Nenhuma inserção realizada.");
            }

            if (templateRepository.count() == 0) {
                templateRepository.save(fichaMedicaCompleta);

                questionRepository.saveAll(List.of(
                        // Dados Pessoais
                        new QuestionModel(null, "Qual é o seu nome completo?", 1L, "Informações Básicas do Paciente", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual sua data de nascimento?", 1L, "Informações Básicas do Paciente", true, QuestionModel.QuestionType.DATE, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual seu gênero?", 1L, "Informações Básicas do Paciente", true, QuestionModel.QuestionType.SELECT, null, fichaMedicaCompleta, "Masculino;Feminino;Transgênero;Não-binário;Prefiro não informar", LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual seu estado civil?", 1L, "Informações Básicas do Paciente", true, QuestionModel.QuestionType.SELECT, null, fichaMedicaCompleta, "Solteiro(a);Casado(a);Divorciado(a);Viúvo(a);Separado(a);Prefiro não informar", LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual sua idade?", 1L, "Informações Básicas do Paciente", true, QuestionModel.QuestionType.NUMBER, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Telefone para contato?", 1L, "Informações Básicas do Paciente", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Informe seu e-mail?", 1L, "Informações Básicas do Paciente", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Informe sua profissão?", 1L, "Informações Básicas do Paciente", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual seu endereço?", 1L, "Informações Básicas do Paciente", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),

                        // Queixa Principal
                        new QuestionModel(null, "Qual é o motivo da sua consulta?", 2L, "Queixa Principal", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Há quanto tempo os sintomas começaram?", 2L, "Queixa Principal", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Sintomas são contínuos ou intermitentes?", 2L, "Queixa Principal", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Descrição detalhada do problema?", 2L, "Queixa Principal", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),

                        // História da doença atual
                        new QuestionModel(null, "Há fatores que agravam ou aliviam os sintomas? Se sim detalhe.", 3L, "História da doença atual", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Já realizou algum tratamento para este problema? Se sim qual?", 3L, "História da doença atual", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),

                        // Histórico médico
                        new QuestionModel(null, "Tem alguma doença crônica? Se sim, detalhe.", 4L, "Histórico médico", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Você já realizou cirurgias? Se sim, quais?", 4L, "Histórico médico", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Já teve internações hospitalares? Se sim, quais?", 4L, "Histórico médico", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Você possui alguma alergia? Se sim, a quê?", 4L, "Histórico médico", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Você toma algum medicamento regularmente? Se sim, quais?", 4L, "Histórico médico", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Você está com suas vacinas em dia? Se não, quais estão pendentes?", 4L, "Histórico médico", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),


                        // Histórico Familiar
                        new QuestionModel(null, "Alguém da sua família tem doencas hereditárias ou crônicas?", 5L, "Histórico Familiar", true, QuestionModel.QuestionType.BOOLEAN, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Seus familiares possuem histórico de hipertensão?", 5L, "Histórico Familiar", true, QuestionModel.QuestionType.BOOLEAN, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Seus familiares possuem histórico de câncer?", 5L, "Histórico Familiar", true, QuestionModel.QuestionType.BOOLEAN, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Seus familiares possuem histórico de doenças cardíacas?", 5L, "Histórico Familiar", true, QuestionModel.QuestionType.BOOLEAN, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Seus familiares possuem histórico de diabetes mellitus?", 5L, "Histórico Familiar", true, QuestionModel.QuestionType.BOOLEAN, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Outros problemas familiares?", 5L, "Histórico Familiar", true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),


                        // Estilo de Vida
                        new QuestionModel(null, "Fuma?", 6L, "Estilo de Vida", false, QuestionModel.QuestionType.BOOLEAN, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Consome bebidas alcoólicas?", 6L, "Estilo de Vida", false, QuestionModel.QuestionType.BOOLEAN, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Você pratica atividades físicas regularmente?", 6L, "Estilo de Vida", true, QuestionModel.QuestionType.BOOLEAN, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Como descreveria sua rotina alimentar?", 6L, "Estilo de Vida", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Quantas horas você dorme por noite?", 6L, "Estilo de Vida", false, QuestionModel.QuestionType.NUMBER, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual é a sua ocupação? Sua rotina de trabalho impacta sua saúde?", 6L, "Estilo de Vida", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),


                        // Avaliação Física
                        new QuestionModel(null, "Qual é a sua altura em centímetros?", 7L, "Avaliação Física", false, QuestionModel.QuestionType.NUMBER, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Peso KG:", 7L, "Avaliação Física", false, QuestionModel.QuestionType.NUMBER, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Frequência cardíaca:", 7L, "Avaliação Física", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Pressão arterial - mmHg:", 7L, "Avaliação Física", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Outros achados?", 7L, "Avaliação Física", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),

                        // Saúde Mental
                        new QuestionModel(null, "Você apresenta sintomas de ansiedade ou depressão?", 8L, "Saúde Mental", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),

                        // Informações Complementares
                        new QuestionModel(null, "Você está com suas vacinas em dia? Se não, quais estão pendentes?", 9L, "Informações Complementares", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Existe algo mais que você gostaria de informar?", 9L, "Informações Complementares", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now())));


            } else {
                System.out.println("TemplateModel - Dados já estão no banco. Nenhuma inserção realizada.");
            }
        };
    }
}




