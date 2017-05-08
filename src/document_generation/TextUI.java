package document_generation;

import document_generation.LawyersLetter.Codes.SectionCode;
import document_generation.LawyersLetter.LLDocument;
import document_generation.LawyersLetter.LLSection;
import document_generation.LawyersLetter.LLSectionFactory;
import document_generation.util.CloseDocument;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Yale Wang
 */
public class TextUI {

    public TextUI(){
        this.setWriting(true);
    }

    public boolean isWriting() {

        return writing;
    }

    public void setWriting(boolean writing) {

        this.writing = writing;
    }

    private boolean writing = false;

    public static void main(String[] args){

        TextUI test = new TextUI();

        LLDocument doc = new LLDocument();
        LLSectionFactory llsf = doc.getLlsf();
        LLSection section = llsf.getSection(doc, SectionCode.IMG);

        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println("Please enter a section code");
        while(test.isWriting()){
            input = sc.nextLine();
            System.out.println("Your input was: " + input + "\n");
            if(input.equalsIgnoreCase("DONE")){
                test.setWriting(false);
                break;
            }
            switch(input.toLowerCase()){
                case "open":
                    section = llsf.getSection(doc, SectionCode.OPEN);
                    break;
                case "re":
                    section = llsf.getSection(doc, SectionCode.RE);
                    break;
                case "emp_desc":
                    section = llsf.getSection(doc, SectionCode.EMP_DESC);
                    break;
                default:
                if(section.getSectionCode() == SectionCode.IMG){
                    //none of the codes given above
                    System.out.println("Not a valid section");
                }
            }
            doc.writeToDoc(section);

        }
        CloseDocument.closeSimple(doc, "/Users/monkhousemacbook6/Documents/JavaCreatedFiles/UITest.docx");

    }

    private void init(LLDocument doc){

    }

}
