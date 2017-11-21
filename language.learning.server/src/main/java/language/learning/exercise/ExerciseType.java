package language.learning.exercise;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum ExerciseType {

	@XmlEnumValue(value = "WORD") WORD, 
	@XmlEnumValue(value = "SENTENCE") SENTENCE, 
	@XmlEnumValue(value = "IMAGE") IMAGE
	
}
