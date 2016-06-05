package nlp;

import com.google.common.collect.Sets;
import pl.sgjp.morfeusz.MorphInterpretation;

import java.util.*;

/**
 * Created by Arjan on 05.06.2016.
 */
public class IsRule implements Rule {

    private static final Set<String> isPhrases = Sets.newHashSet("być", "zostać");
    private static final String TRUE = "true";

    @Override
    public List<ObjectProperty> checkRule(List<MorphInterpretation> results, int position) {
        if(results.size() < position+2) {
            return new ArrayList<>(0);
        }
        MorphInterpretation object = results.get(position);
        MorphInterpretation verb = results.get(position+1);
        MorphInterpretation property = results.get(position+2);

        if(!isPhrases.contains(verb.getLemma())) {
            return new ArrayList<>(0);
        }

        return Collections.singletonList(new ObjectProperty(object.getLemma(), property.getLemma(), TRUE));
    }

}
