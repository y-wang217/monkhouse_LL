package document_generation.LawyersLetter;

import document_generation.util.ManipDocument;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yale Wang
 */
public class LLDocument extends XWPFDocument {

    public LLDocument() {

        super();
        init();
    }

    private void init() {

        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        this.setFieldsMap(fields);
        LLSectionFactory llsf = new LLSectionFactory();
        this.setLlsf(llsf);
    }

    private LinkedHashMap<String, String> fieldsMap;
    private LLSectionFactory llsf;

    public void writeToDoc(LLSection section) {

        for (LLParagraph p : section.getContents()) {
            for (String s : p.getText().split("%%")) {
                //TODO replace "<*>" with the respective field
                processFields(s);
                System.out.println(p.getParaType() + " : " + s);
                XWPFRun r = ManipDocument.createRun(p.getXwpfParagraph());
                ManipDocument.tab(r);
                alterRun(r, p);
                ManipDocument.append(r, 1, s);
            }
        }
    }

    private void alterRun(XWPFRun r, LLParagraph llParagraph) {

        r.setBold(llParagraph.isBold());
        r.setItalic(llParagraph.isItalics());
        if (llParagraph.isUnderline()) r.setUnderline(UnderlinePatterns.SINGLE);
    }

    private Pattern PATTERN_FOR_FIELDS = Pattern.compile("<(.*?)>");
    private void processFields(String s){
        Matcher m = PATTERN_FOR_FIELDS.matcher(s);
        while(m.find()){
            System.out.println(m.group(1));
        }
    }

    public LLSectionFactory getLlsf() {

        return llsf;
    }

    private void setLlsf(LLSectionFactory llsf) {

        this.llsf = llsf;
    }

    public LinkedHashMap<String, String> getFieldsMap() {

        return fieldsMap;
    }

    private void setFieldsMap(LinkedHashMap<String, String> fieldsMap) {

        this.fieldsMap = fieldsMap;
    }
}
