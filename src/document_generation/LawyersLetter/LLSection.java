package document_generation.LawyersLetter;

import document_generation.LawyersLetter.Codes.ParaCode;
import document_generation.LawyersLetter.Codes.SectionCode;

import java.util.ArrayList;

/**
 * Created by Yale Wang
 *
 * note: contents stores paragraphs in an Arraylist
 */
public class LLSection {

    public LLParagraph insertText(LLParagraphFactory llpf, LLDocument doc, ArrayList<LLParagraph> content,
                                  ParaCode type, String textInPara){
        LLParagraph llParagraph = llpf.getParagraph(doc, type);
        llParagraph.setText(textInPara);
        content.add(llParagraph);
        return llParagraph;
    }

    private ArrayList<LLParagraph>contents;
    private SectionCode sectionCode;

    public SectionCode getSectionCode() {

        return sectionCode;
    }

    public void setSectionCode(SectionCode sectionCode) {

        this.sectionCode = sectionCode;
    }

    public void setContents(ArrayList contents) {

        this.contents = contents;
    }

    public ArrayList<LLParagraph> getContents() {

        return contents;
    }
}
