package document_generation.LawyersLetter;

import document_generation.LawyersLetter.Codes.SectionCode;
import document_generation.LawyersLetter.Sections.EmploymentDescriptionSection;
import document_generation.LawyersLetter.Sections.HeaderImageSection;
import document_generation.LawyersLetter.Sections.OpeningSection;
import document_generation.LawyersLetter.Sections.ReCaseSection;

/**
 * Created by Yale Wang
 *
 * TODO: currently, text is hard-coded into setters. Eventually move to extracting from DB
 */
public class LLSectionFactory {

    private LLParagraphFactory llpf = new LLParagraphFactory();

    public LLSection getSection(LLDocument doc, SectionCode type){

        switch(type){
            case IMG: return new HeaderImageSection(doc, llpf);
            case OPEN: return new OpeningSection(doc, llpf);
            case RE:return new ReCaseSection(doc, llpf);
            case EMP_DESC: return new EmploymentDescriptionSection(doc, llpf);
            default: return new LLSection();
        }
    }

}
