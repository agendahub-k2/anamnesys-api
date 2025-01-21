package com.anamnesys.controller;

import com.anamnesys.repository.TermRepository;
import com.anamnesys.repository.model.TermModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/term")
public class TermController {

    @Autowired
    private TermRepository termRepository;
    private static final Logger logger = LoggerFactory.getLogger(TermController.class);

    @GetMapping
    public ResponseEntity<List<TermModel>> getAllTemplates() {
        logger.info("Received request get all terms");
        List<TermModel> templates = termRepository.findByUserIdIsNull();
        logger.info("Process get all templates");
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TermModel>> getAllTemplatesByUserId(@PathVariable Long userId) {
        logger.info("Received request get all terms by user id");
        List<TermModel> templates = termRepository.findByUserId(userId);
        logger.info("Process get all templates by user id");
        return ResponseEntity.ok(templates);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<TermModel> createTerm(@RequestBody TermModel termModel, @PathVariable Long userId) {
        logger.info("Received request to create a new term");

        if (termModel == null || termModel.getTerm() == null || termModel.getName() == null || termModel.getTerm().isEmpty() || userId == null) {
            logger.error("Term data is missing or invalid");
            return ResponseEntity.badRequest().build();
        }

        termModel.setUserId(userId);

        TermModel savedTerm = termRepository.save(termModel);
        logger.info("Term created successfully with ID: {}", savedTerm.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTerm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerm(@PathVariable Long id) {
        logger.info("Received request to delete term with ID: {}", id);

        if (!termRepository.existsById(id)) {
            logger.error("Term with ID {} not found", id);
            return ResponseEntity.notFound().build(); // Retorna 404 se o termo não for encontrado
        }

        termRepository.deleteById(id);
        logger.info("Term with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build(); // Retorna 204 após a exclusão com sucesso
    }
}

