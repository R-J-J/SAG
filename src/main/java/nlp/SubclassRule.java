package nlp;

import com.google.common.collect.Sets;
import pl.sgjp.morfeusz.MorphInterpretation;

import java.util.*;

/**
 * Created by Arjan on 05.06.2016.
 */
public class SubclassRule extends Rule {

    private static final Set<String> subClassPhrases = Sets.newHashSet("być", "zostać");
    private static final String TRUE = "true";

    public SubclassRule(AnalysisBuilder analysisBuilder) {
        super(analysisBuilder);
    }

    @Override
    public void checkRule(List<MorphInterpretation> results, int position) {
        if(results.size() < position+2) {
            return;
        }
        MorphInterpretation object = results.get(position);
        MorphInterpretation verb = results.get(position+1);
        MorphInterpretation subclass = results.get(position+2);

        if(!subClassPhrases.contains(verb.getLemma())) {
            return;
        }

        analysisBuilder.addObjectSubclass(new ObjectSubclass(object.getLemma(), subclass.getLemma()));
    }

}
