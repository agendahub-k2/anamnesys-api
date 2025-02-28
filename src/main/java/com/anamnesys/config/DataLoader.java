package com.anamnesys.config;

import com.anamnesys.repository.QuestionRepository;
import com.anamnesys.repository.SegmentRepository;
import com.anamnesys.repository.TemplateRepository;
import com.anamnesys.repository.TermRepository;
import com.anamnesys.repository.model.QuestionModel;
import com.anamnesys.repository.model.SegmentModel;
import com.anamnesys.repository.model.TemplateModel;
import com.anamnesys.repository.model.TermModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataLoader {

    public static final String TERMO_DE_COMPROMISSO = "Declaro que as informações fornecidas nesta ficha são completas, corretas e verdadeiras. "
            + "Estou ciente de que esta ficha de anamnese tem como finalidade auxiliar no atendimento e que as informações aqui contidas serão tratadas "
            + "de acordo com a legislação vigente, incluindo a Lei Geral de Proteção de Dados (LGPD). "
            + "Compreendo que este documento não substitui uma consulta médica ou de um profissional especializado. "
            + "Autorizo o uso dos dados para fins exclusivamente relacionados ao atendimento e aceito.";

    public static final String TERMO_DE_COMPROMISSO_TATUAGEM = "Declaro que estou ciente dos riscos associados ao procedimento de tatuagem...";

    @Bean
    public CommandLineRunner loadData(SegmentRepository segmentRepository,
                                      TemplateRepository templateRepository,
                                      QuestionRepository questionRepository,
                                      TermRepository termRepository) {

        return args -> {
            switch ((int) templateRepository.count()) {
                case 0:
                    saveTemplateMedicaHistorico(templateRepository, termRepository, segmentRepository);
                    saveTemplateTatuadorModel(templateRepository, termRepository, segmentRepository);
                    saveTemplateTatuadoresModel(templateRepository, segmentRepository);
                    saveTemplateTatuadorCompleta(templateRepository, segmentRepository);
                    break;
                case 1:
                    saveTemplateTatuadorModel(templateRepository, termRepository, segmentRepository);
                    saveTemplateTatuadoresModel(templateRepository, segmentRepository);
                    saveTemplateTatuadorCompleta(templateRepository, segmentRepository);

                    break;
                default:
                    break;

            }
        };
    }

    @Transactional
    private void saveTemplateMedicaHistorico(TemplateRepository templateRepository, TermRepository termRepository, SegmentRepository segmentRepository) {
        SegmentModel saude = new SegmentModel(null, "Saúde", "Foco em histórico médico e alergias.", "médico");
        segmentRepository.save(saude);

        TemplateModel fichaMedicaCompleta = new TemplateModel(
                null,
                "Ficha de Anamnese Médica",
                saude,
                "Coleta detalhada de informações médicas para uso geral em saúde.",
                new ArrayList<>()
        );

        fichaMedicaCompleta.getQuestions().addAll(
                List.of(

                        // Informações Complementares
                        new QuestionModel(null, "Você está com suas vacinas em dia? Se não, quais estão pendentes?", 9L, "Informações Complementares", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Existe algo mais que você gostaria de informar?", 9L, "Informações Complementares", false, QuestionModel.QuestionType.TEXT, null, fichaMedicaCompleta, null, LocalDateTime.now(), LocalDateTime.now())
                )
        );

        TermModel term = new TermModel(null, "Termo padrão anamnese", TERMO_DE_COMPROMISSO, null, null, null);
        termRepository.save(term);
        templateRepository.save(fichaMedicaCompleta);
    }

    @Transactional
    private static void saveTemplateTatuadorModel(TemplateRepository templateRepository, TermRepository termRepository, SegmentRepository segmentRepository) {

        SegmentModel esteticaBeleza = new SegmentModel(null, "Estética e Beleza", "Foco em procedimentos estéticos, incluindo tatuagem.", "tatuador");
        segmentRepository.save(esteticaBeleza);
        TemplateModel fichaTatuagem = new TemplateModel(
                null,
                "Ficha de Anamnese para Tatuagem",
                esteticaBeleza,
                "Coleta detalhada de informações relevantes para o procedimento de tatuagem.",
                new ArrayList<>()
        );



        fichaTatuagem.getQuestions().addAll(
                List.of(
                        new QuestionModel(null, "Qual é o seu nome completo?", 1L, "Dados Pessoais", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual sua data de nascimento?", 1L, "Dados Pessoais", true, QuestionModel.QuestionType.DATE, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Telefone para contato?", 1L, "Dados Pessoais", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Você possui alergia a tinta, látex ou outros produtos químicos?", 2L, "Histórico de Saúde", true, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Tem alguma condição de saúde que possa interferir no procedimento? (ex: diabetes, hemofilia)", 2L, "Histórico de Saúde", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Está em uso de algum medicamento? Se sim, qual?", 2L, "Histórico de Saúde", false, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Já realizou tatuagem antes? Teve alguma complicação?", 3L, "Histórico de Tatuagem", false, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual a região do corpo onde deseja a tatuagem?", 3L, "Detalhes do Procedimento", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Possui alguma cicatriz, queloide ou problema de pele na área da tatuagem?", 3L, "Detalhes do Procedimento", true, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now())
                )
        );
        TermModel termTatuagem = new TermModel(null, "Termo padrão tatuagem", TERMO_DE_COMPROMISSO_TATUAGEM, null, null, null);
        termRepository.save(termTatuagem);
        templateRepository.save(fichaTatuagem);
    }

    @Transactional
    private static void saveTemplateTatuadoresModel(TemplateRepository templateRepository, SegmentRepository segmentRepository) {

        SegmentModel tatuador = segmentRepository.findByNameAndCategory("Estética e Beleza", "tatuador");

        // Verifica se encontrou o segmento no banco
        if (tatuador == null) {
            tatuador = new SegmentModel(null, "Estética e Beleza", "Categoria para tatuadores", "tatuador");
            segmentRepository.save(tatuador); // Se não existir, salva primeiro
        } else {
            tatuador = segmentRepository.findById(tatuador.getId()).orElseThrow(); // Garante que está gerenciado
        }

        TemplateModel fichaTatuagem = new TemplateModel(
                null,
                "Ficha de Anamnese para Tatuadores",
                tatuador, // Já está gerenciado
                "Formulário para avaliar a saúde e a preparação do cliente antes do procedimento de tatuagem.",
                new ArrayList<>()
        );

        fichaTatuagem.getQuestions().addAll(
                List.of(
                        new QuestionModel(null, "Nome completo:", 1L, "Dados Pessoais", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Data de nascimento:", 1L, "Dados Pessoais", true, QuestionModel.QuestionType.DATE, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Telefone de contato:", 1L, "Dados Pessoais", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),

                        new QuestionModel(null, "Você tem alguma alergia a pigmentos, anestésicos ou produtos químicos?", 2L, "Histórico de Saúde", true, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Possui alguma condição de saúde que afete a cicatrização? (ex: diabetes, problemas circulatórios)", 2L, "Histórico de Saúde", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Está utilizando algum medicamento anticoagulante ou que afete a cicatrização?", 2L, "Histórico de Saúde", false, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),

                        new QuestionModel(null, "Já fez tatuagem antes? Se sim, teve alguma reação adversa?", 3L, "Histórico de Tatuagem", false, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual região do corpo deseja tatuar?", 3L, "Detalhes do Procedimento", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Você se expõe frequentemente ao sol sem proteção solar?", 3L, "Cuidados com a Pele", true, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Possui cicatrizes, queloides ou problemas dermatológicos na área da tatuagem?", 3L, "Cuidados com a Pele", true, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),

                        new QuestionModel(null, "Consumiu álcool ou drogas nas últimas 24 horas?", 4L, "Segurança do Procedimento", true, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Quais são suas expectativas em relação à tatuagem? Alguma referência específica?", 5L, "Expectativas e Design", false, QuestionModel.QuestionType.TEXT, null, fichaTatuagem, null, LocalDateTime.now(), LocalDateTime.now())
                )
        );

        templateRepository.save(fichaTatuagem);
    }



    private static void saveTemplateTatuadorCompleta(TemplateRepository templateRepository, SegmentRepository segmentRepository) {


        SegmentModel tatuador = segmentRepository.findByNameAndCategory("Estética e Beleza","tatuador");
        tatuador = segmentRepository.findById(tatuador.getId()).orElseThrow();

        TemplateModel fichaTatuagemCompleta = new TemplateModel(
                null,
                "Ficha de Anamnese para Tatuagem - Completa",
                tatuador,
                "Coleta detalhada de informações relevantes para o procedimento de tatuagem, garantindo segurança e cuidados adequados.",
                new ArrayList<>()
        );

        fichaTatuagemCompleta.getQuestions().addAll(
                List.of(
                        // **Dados Pessoais**
                        new QuestionModel(null, "Qual é o seu nome completo?", 1L, "Dados Pessoais", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual sua data de nascimento?", 1L, "Dados Pessoais", true, QuestionModel.QuestionType.DATE, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Telefone para contato?", 1L, "Dados Pessoais", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Você possui plano de saúde?", 1L, "Dados Pessoais", false, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),

                        // **Histórico de Saúde**
                        new QuestionModel(null, "Você possui alguma alergia? (Tintas, anestésicos, látex, medicamentos, alimentos, etc.)", 2L, "Histórico de Saúde", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Tem alguma condição de saúde que possa interferir no procedimento? (Diabetes, hipertensão, hemofilia, epilepsia, doenças autoimunes, etc.)", 2L, "Histórico de Saúde", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Está em uso de algum medicamento? Se sim, qual?", 2L, "Histórico de Saúde", false, QuestionModel.QuestionType.TEXT, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Faz uso frequente de álcool ou drogas?", 2L, "Histórico de Saúde", false, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Está grávida ou amamentando?", 2L, "Histórico de Saúde", false, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),

                        // **Histórico de Tatuagem**
                        new QuestionModel(null, "Já realizou tatuagem antes? Se sim, teve alguma complicação?", 3L, "Histórico de Tatuagem", false, QuestionModel.QuestionType.TEXT, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Já fez algum procedimento estético na área a ser tatuada? (Exemplo: peeling, laser, micropigmentação)", 3L, "Histórico de Tatuagem", false, QuestionModel.QuestionType.TEXT, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),

                        // **Detalhes do Procedimento**
                        new QuestionModel(null, "Qual a região do corpo onde deseja a tatuagem?", 4L, "Detalhes do Procedimento", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Qual o estilo e tamanho aproximado da tatuagem?", 4L, "Detalhes do Procedimento", true, QuestionModel.QuestionType.TEXT, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Possui alguma cicatriz, queloide ou problema de pele na área da tatuagem?", 4L, "Detalhes do Procedimento", true, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),

                        // **Cuidados Pré-Tatuagem**
                        new QuestionModel(null, "Você recebeu instruções sobre os cuidados pré-tatuagem? (Exemplo: hidratar a pele, evitar exposição ao sol, evitar bebidas alcoólicas)", 5L, "Cuidados Pré-Tatuagem", true, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),

                        // **Cuidados Pós-Tatuagem**
                        new QuestionModel(null, "Você compreende e concorda em seguir as orientações para o cuidado da tatuagem após o procedimento?", 6L, "Cuidados Pós-Tatuagem", true, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),

                        // **Consentimento Legal**
                        new QuestionModel(null, "Você autoriza o tatuador a utilizar imagens da sua tatuagem para portfólio e redes sociais?", 7L, "Consentimento Legal", false, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now()),
                        new QuestionModel(null, "Você leu e concorda com os termos e condições do procedimento?", 7L, "Consentimento Legal", true, QuestionModel.QuestionType.BOOLEAN, null, fichaTatuagemCompleta, null, LocalDateTime.now(), LocalDateTime.now())
                )
        );

        templateRepository.save(fichaTatuagemCompleta);
    }


}




