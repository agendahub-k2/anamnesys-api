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

            // Verifica se o template já existe, caso contrário, salva
            if (templateRepository.count() == 0) {
                templateRepository.save(fichaMedicaCompleta);

                // Associa todas as perguntas ao template da ficha médica completa
                questionRepository.saveAll(List.of(
                        // Dados Pessoais
                        new QuestionModel(null, "Qual é o seu nome completo?", 1L, "Informações Básicas do Paciente" , true, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual sua data de nascimento?", 1L, "Informações Básicas do Paciente",true, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Gênero?", 1L, "Informações Básicas do Paciente",true, QuestionModel.QuestionType.SELECT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual seu estado civil?", 1L, "Informações Básicas do Paciente",true, QuestionModel.QuestionType.SELECT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual sua idade?", 1L,"Informações Básicas do Paciente", true, QuestionModel.QuestionType.NUMBER,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Telefone para contato?", 1L,"Informações Básicas do Paciente" ,true, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Informe seu e-mail?", 1L, "Informações Básicas do Paciente",true, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Informe sua profissão?", 1L, "Informações Básicas do Paciente",false, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual seu endereço?", 1L, "Informações Básicas do Paciente",false, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),

                        // Queixa Principal
                        new QuestionModel(null, "Qual é o motivo da sua consulta?", 2L,"Queixa Principal" ,true, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),

                        // Histórico Médico
                        new QuestionModel(null, "Quais doenças você já teve?", 3L, true, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Você já realizou cirurgias? Se sim, quais?", 3L, false, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Já teve internações hospitalares? Se sim, quando e por qual motivo?", 3L, false, QuestionModel.QuestionType.TEXT, null,fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Possui algum diagnóstico atual?", 3L, true, QuestionModel.QuestionType.TEXT, null,fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),

                        // Medicamentos
                        new QuestionModel(null, "Você toma algum medicamento regularmente? Se sim, quais?", 4L, true, QuestionModel.QuestionType.TEXT, null,fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Está em tratamento com algum especialista? Se sim, qual especialidade?", 4L, false, QuestionModel.QuestionType.TEXT, null,fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),

                        // Alergias e Sensibilidades
                        new QuestionModel(null, "Você possui alguma alergia? Se sim, a quê?", 5L, true, QuestionModel.QuestionType.TEXT, null,fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Já teve reações adversas a medicamentos ou alimentos? Se sim, quais?", 5L, false, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),

                        // Histórico Familiar
                        new QuestionModel(null, "Seus familiares possuem histórico de diabetes?", 6L, false, QuestionModel.QuestionType.BOOLEAN, null,fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Seus familiares possuem histórico de hipertensão?", 6L, false, QuestionModel.QuestionType.BOOLEAN,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Seus familiares possuem histórico de câncer?", 6L, false, QuestionModel.QuestionType.BOOLEAN,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Outros problemas familiares?", 6L, false, QuestionModel.QuestionType.TEXT, null,fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),

                        // Estilo de Vida
                        new QuestionModel(null, "Você pratica atividades físicas regularmente?", 7L, true, QuestionModel.QuestionType.BOOLEAN,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Como descreveria sua rotina alimentar?", 7L, false, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Consome bebidas alcoólicas?", 7L, false, QuestionModel.QuestionType.BOOLEAN,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Fuma?", 7L, false, QuestionModel.QuestionType.BOOLEAN, null,fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Quantas horas você dorme por noite?", 7L, false, QuestionModel.QuestionType.NUMBER,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),

                        // Avaliação Psicológica
                        new QuestionModel(null, "Como estão seus níveis de estresse nos últimos meses?", 8L, false, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Já foi diagnosticado com algum transtorno psicológico? Se sim, qual?", 8L, false, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),

                        // Exames Recentes
                        new QuestionModel(null, "Você realizou exames nos últimos 6 meses? Se sim, quais?", 9L, false, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now()),

                        // Informações Complementares
                        new QuestionModel(null, "Existe algo mais que você gostaria de informar?", 10L, false, QuestionModel.QuestionType.TEXT,null, fichaMedicaCompleta, LocalDateTime.now(), LocalDateTime.now())
                ));
            } else {
                System.out.println("TemplateModel - Dados já estão no banco. Nenhuma inserção realizada.");
            }
        };
    }
}




