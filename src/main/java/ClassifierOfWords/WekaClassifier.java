package ClassifierOfWords;

public class WekaClassifier {

    public static void main(String[] args) {
        Classify wordVector =  new Classify();

        System.out.println("Classification: " + wordVector.classifyObject("0,-1,0,1,1,0,0,1,0,0,0",
                "modelNelubimaya"));
        System.out.println("Classification: " + wordVector.classifyObject("0,-1,0,1,1,0,0,1,0,0,0",
                "modelLidianka"));
        System.out.println("Classification: " + wordVector.classifyObject("0,1,1,18,16,0,0,8,0,0,0",
                "modelNelubimaya"));
        System.out.println("Classification: " + wordVector.classifyObject("0,1,1,18,16,0,0,8,0,0,0",
                "modelLidianka"));
        System.out.println("Classification: " + wordVector.classifyObject("0,1,0,1,2,0,0,1,0,0,0",
                "modelNelubimaya"));
        System.out.println("Classification: " + wordVector.classifyObject("0,1,0,1,2,0,0,1,0,0,0",
                "modelLidianka"));




        //0,1,0,1,2,0,0,1,0,0,0,порыв 1,-1,1,2,1,0,0,1,0,0,0,инна
    }
}
//0,-1,1,4,3,0,0,2,0,0,0,девочка,'person'
//0,-1,1,1,1,0,0,1,0,0,0,брюнетка,'person'
//0,1,1,18,16,0,0,8,0,0,0,папочка,'person'
//0,1,0,2,1,0,0,1,0,0,0,обед /ДЛЯ ТЕСТА
//0,1,0,1,0,0,0,0,0,0,0,алфавит,'something'
//0,0,0,1,0,0,0,0,0,0,0,ребята,'something'
//0,1,0,2,0,0,0,0,0,0,0,голосок,'something'
