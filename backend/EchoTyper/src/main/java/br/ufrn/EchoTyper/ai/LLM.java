import com.openai.OpenAIClient;
import com.openai.OpenAIOkHttpClient;
import com.openai.api.chat.ChatCompletionCreateParams;
import com.openai.api.chat.ChatMessage;
import com.openai.api.chat.ChatModel;
import com.openai.api.chat.ChatCompletionChoice;

import java.util.List;

// TODO This is incomplete
public class LLM {
    public String resume(String text) {
        OpenAIClient client = OpenAIOkHttpClient.fromEnv();

        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
            .model(ChatModel.GPT_4O_MINI)
            .messages(List.of(
                ChatMessage.userMessage("Please summarize the following text in topics: " + text)
            ))
            .build();

        List<ChatCompletionChoice> choices = client.chat()
            .completions()
            .create(createParams)
            .choices();

        return choices.get(0).message().content().orElse("No response.");
    }
}

