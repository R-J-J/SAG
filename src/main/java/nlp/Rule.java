package nlp;

import pl.sgjp.morfeusz.MorphInterpretation;

import java.util.List;

/**
 * Created by Arjan on 05.06.2016.
 */
public interface Rule {

    List<ObjectProperty> checkRule(List<MorphInterpretation> results, int position);

}
