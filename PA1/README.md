# PA1
Student Name: Abdolrasoul Sharifi
Git Username: danteisalive
Computing ID: as3mx

error_case_#1.in and error_case_#1.in.OK are added to test whether the scanner is able to find symbols that don't appear in the terminals doc e.g., @, $, %, &

error_case_#2.in and error_case_#2.in.OK are added to test whether the scanner is able to find  badly-formed tokens

Modified ./PA1Start/Makefile line number 67:

+++ 	java -jar java-cup-11a.jar -parser  mj -dump $(PARSE_DIR)/mj.cup > javacup.dump 2>&1

---   java -jar java-cup-11a.jar -parser mj -dump $(PARSE_DIR)/mj.cup >& javacup.dump

Modified ./PA1Start/Makefile line number 56:

+++   cd $(SRC_DIR); $(JAR) cmf $(SCANNER)MainClass.txt $(SCANNER).jar *.class */*.class -C $(JAVA_CUP_RUNTIME) java_cup

---   cd $(SRC_DIR); $(JAR) cmf $(SCANNER)MainClass.txt $(SCANNER).jar *.class */*.class */*/*.class -C $(JAVA_CUP_RUNTIME) java_cup


Modified ./PA1Start/TestCases/regress.sh line number 11:

+++   java -jar ../MJPA2.jar $filename  > t 2>&1

---   java -jar ../MJPA2.jar $filename >&t
