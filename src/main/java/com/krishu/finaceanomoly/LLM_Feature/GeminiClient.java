package com.krishu.finaceanomoly.LLM_Feature;

import com.krishu.finaceanomoly.DTO.GeminiResponse;
import com.krishu.finaceanomoly.DTO.LLmCategorizeResult;
import com.krishu.finaceanomoly.ExpenseCategory;
import com.krishu.finaceanomoly.Model.Expense;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class GeminiClient implements LLMClient{

    @Value("${gemini_key}")
    private String key;
    private final RestClient restClient = RestClient.builder().build();
    private final ObjectMapper mapper=new ObjectMapper();
    private final String url="https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-lite:generateContent";


    @Override
    public LLmCategorizeResult categorize(Expense expense) {
        String prompt=buildPrompt(expense);
        Map<String,Object> request=Map.of("contents", List.of(Map.of("parts",List.of(Map.of("text",prompt)))));
        GeminiResponse response=restClient.post().uri(url+"?key="+key).contentType(MediaType.APPLICATION_JSON).body(request).retrieve().body(GeminiResponse.class);
        return parseResponse(response);
    }


    private LLmCategorizeResult parseResponse(GeminiResponse response) {
        try {
            String generatedJson=response.candidates().getFirst().content().parts().getFirst().text();
            generatedJson = generatedJson.replace("```json", "").replace("```", "").trim();
            return mapper.readValue(generatedJson, LLmCategorizeResult.class);
        } catch (Exception e) {
            return new LLmCategorizeResult(ExpenseCategory.OTHER, false, 0.0, "AI categorization failed: " + e.getMessage());
        }
    }

    private String buildPrompt(Expense expense){
        return """
        You are a finance controller reviewing an expense. Respond ONLY with valid JSON, no other text.
        Expense details:
        Vendor: %s
        Amount: %s %s
        Description: %s
        Respond with this exact JSON structure:
        {"category": "EXACTLY one of these strings, case-sensitive: TRAVEL, MEAL, SOFTWARE, OFFICE_SUPPLIES, ACCOMMODATION, OTHER",
         "anomalyDetected": true or false,
         "confidence": a number between 0 and 1,
         "reasoning": "one sentence explaining your categorization and any anomaly concern"}
        """.formatted(expense.getVendor(), expense.getAmount(), expense.getCurrency(), expense.getDescription());
    }
}
