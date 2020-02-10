import com.github.llyb120.json.Obj;
import org.junit.Test;

import static com.github.llyb120.json.Json.a;
import static com.github.llyb120.json.Json.o;

public class mongo {

    @Test
    public void test(){
        Obj obj;
         String query = "orgs.*[a=1||a=2]";
         query = "*[][a=1]";
         obj = o("a", 1);

         query = "b[a=1]";
         obj = o("b", o("a", 1));

         query = "b.a=1,b.a=2";
         obj = o("$or", a(
                 o("b", o("a", 1)),
                 o("b", o("a", 2))
         ));

         query = "[b.a=1][b.a=2]";
         query = "b[a=1][a=2]";
         obj = o("$and", a(
                 o("b", o("a", 1)),
                 o("b", o("a", 2))
         ));

         query = "orgs[][type = ?][id in ?]";
         obj = o(
                 "orgs", o(
                         "$elemMatch", o(
                                 "type", "DEP",
                                 "id", o(
                                         "$in", a(1,2,3)
                                 )
                         )
                 )
         );

         query = "[a=1]";
         query = "[b=2]";
         query = "a=1||a=2";
    }
}
