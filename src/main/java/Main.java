import annotations.processor.JsonSerializeProcessor;
import annotations.test.PersonModel;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start library");
        PersonModel p = new PersonModel("Juan", "juan@gmail.com", "12");
        String json = JsonSerializeProcessor.converToJson(p);
        System.out.println("JSON = " + json);
    }
}
