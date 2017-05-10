package document_generation.LawyersLetter;

import document_generation.LawyersLetter.Codes.ParaCode;
import document_generation.LawyersLetter.Codes.SectionCode;
import document_generation.util.CloseDocument;
import document_generation.util.ManipDocument;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static document_generation.LawyersLetter.Codes.ParaCode.TAB;

/**
 * Created by Yale Wang
 */
public class LLDocument extends XWPFDocument {

    //CONSTRUCTOR
    public LLDocument() {

        super();
        init();
    }

    private enum gender {m, f}

    //PRIVATE INITIALIZER TO SEPARATE TEST EXECUTION
    private void init() {

        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        this.setFieldsMap(fields);
        LLSectionFactory llsf = new LLSectionFactory();
        this.setLlsf(llsf);

        boolean testForFieldsFromInput = false;//TODO flag for if fields are prompted
        promptForGender(testForFieldsFromInput);
        promptForFields(testForFieldsFromInput);
        initFields(testForFieldsFromInput);
    }

    //initialize fields from default/testing fieldsMap here:
    private void initFields(boolean testForFieldsFromInput) {

        if (!testForFieldsFromInput) {
            LinkedHashMap<String, String> defaultFieldsMap = new LinkedHashMap<>();
            defaultFieldsMap.put("monkhouse_lawyer_name", "Andrew Monkhouse, J.D.");
            defaultFieldsMap.put("monkhouse_lawyer_email", "andrew@monkhouselaw.com");
            defaultFieldsMap.put("OC_HR_first_name", "Opposing");
            defaultFieldsMap.put("OC_HR_last_name", "Lawyer");
            defaultFieldsMap.put("OC_HR_job_title", "");
            defaultFieldsMap.put("OC_HR_company_name", "Some Law Office LLP");
            defaultFieldsMap.put("OC_HR_company_address", "123 Bay Street");
            defaultFieldsMap.put("OC_HR_company_postcode", "M5J 1A1");

            defaultFieldsMap.put("employer_last_name", "");
            defaultFieldsMap.put("employer_first_name", "");
            defaultFieldsMap.put("client_last_name", "John");
            defaultFieldsMap.put("client_first_name", "Doe");

            defaultFieldsMap.put("seniority_in_years", "10");
            defaultFieldsMap.put("wage_in_dollars", "75,000.00 per year, plus etc.");
            defaultFieldsMap.put("age", "45");
            defaultFieldsMap.put("client_position", "Manager/Specialist");

            defaultFieldsMap.put("termination_date", "May 1, 2017");
            defaultFieldsMap.put("monkhouse_lawyer_title", "Senior Lawyer & Founder");
            this.setFieldsMap(defaultFieldsMap);
        }
        initFields();
    }

    //SET UP AND INITLIZE CERTAIN FIELDS
    private void initFields() {

        LinkedHashMap<String, String> defaultFieldsMap = this.getFieldsMap();

        gender g = this.getClientGender();

        defaultFieldsMap.put("honorific", (g==gender.m)?"Mr.":"Mrs.");

        defaultFieldsMap.put("subject_pronoun", (g==gender.m)?"he":"she");
        defaultFieldsMap.put("subject_pronoun_caps", (g==gender.m)?"He":"She");

        defaultFieldsMap.put("object_pronoun", (g==gender.m)?"him":"her.");
        defaultFieldsMap.put("object_pronoun_caps", (g==gender.m)?"Him":"Her.");

        defaultFieldsMap.put("possessive_pronoun", (g==gender.m)?"his":"her");
        defaultFieldsMap.put("possessive_pronoun_caps", (g==gender.m)?"His":"Her");


        this.setFieldsMap(defaultFieldsMap);
    }

    //gender determines which pronouns are used
    private gender clientGender;
    private void promptForGender(boolean testForFieldsFromInput) {

        if (testForFieldsFromInput) {
            Scanner sc = new Scanner(System.in);
            String input;
            input = sc.nextLine();

            switch (input.toLowerCase()) {
                case "male":
                case "m":
                    setClientGender(gender.m);
                case "female":
                case "f":
                    setClientGender(gender.f);
                default:
                    if (this.getClientGender() != gender.f && this.getClientGender() != gender.m) {
                        System.out.println("Please enter a valid gender");
                        promptForGender(true);
                    }
            }
        }
    }

    //some fields for testing fields list prompt
    private String[] initialFieldsList = {"employer_last_name", "employer_first_name", "client_last_name", "client_first_name"};
    private void promptForFields(boolean testFields) {

        if (testFields) {
            Scanner sc = new Scanner(System.in);
            String input;

            System.out.println("Please enter a section code");
            for (String field : initialFieldsList) {
                System.out.println("Please enter the: " + field);
                input = sc.nextLine();
                fieldsMap.put(field, input);
            }
        }
        for (String key : fieldsMap.keySet()) {
            System.out.println(" (" + key + ", " + fieldsMap.get(key) + ") ");
        }
    }

    //PRIVATE FIELDS
    private LinkedHashMap<String, String> fieldsMap;
    private LLSectionFactory llsf;

    //WRITE SECTIONS TO DOCUMENT
    public void writeToDoc(LLSection section) {

        for (LLParagraph p : section.getContents()) {
            for (String s : p.getText().split("%%")) {
                s = processFields(s);
                System.out.println(p.getParaType() + " : " + s);
                XWPFRun r = ManipDocument.createRun(p.getXwpfParagraph());
                if (p.getParaType().equals(TAB)) ManipDocument.tab(r);
                alterRun(r, p);
                ManipDocument.append(r, 1, s);
            }
        }
    }

    //METHOD FOR ADDING FORMATTING TO RUNS
    private void alterRun(XWPFRun r, LLParagraph llParagraph) {

        r.setBold(llParagraph.isBold());
        r.setItalic(llParagraph.isItalics());
        if (llParagraph.isUnderline()) r.setUnderline(UnderlinePatterns.SINGLE);
    }

    //SEARCH REGEX FOR FIELDS IN "<" AND ">", REPLACE WITH FIELDS FROM MAP
    private Pattern PATTERN_FOR_FIELDS = Pattern.compile("<(.*?)>");

    private String processFields(String s) {

        String fieldReplaced = s;
        Matcher m = PATTERN_FOR_FIELDS.matcher(s);
        while (m.find()) {
            String fieldName = m.group(1);
            System.out.println("   " + fieldName);
            fieldReplaced = fieldReplaced.replace("<" + fieldName + ">", fieldsMap.getOrDefault(fieldName, "###field not found error###"));
        }
        return fieldReplaced;
    }

    //SETTERS AND GETTERS
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

    private gender getClientGender() {

        return clientGender;
    }

    private void setClientGender(gender clientGender) {

        this.clientGender = clientGender;
    }

    public static void main(String[] args) {

        LLDocument test = new LLDocument();

        LLSection s = test.getLlsf().getSection(test, SectionCode.CUSTOM);
        ArrayList<LLParagraph> listOfParas = new ArrayList<>();
        s.insertText(new LLParagraphFactory(), test, listOfParas, ParaCode.REG, "<employer_last_name> is the employer's last night " +
                "<employer_first_name> is the employer's first name " +
                "<client_last_name> is the client's last name " +
                "<client_first_name> is the client's first name");
        s.setContents(listOfParas);

        test.writeToDoc(s);
        CloseDocument.closeSimple(test);
    }

}
