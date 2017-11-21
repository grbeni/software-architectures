package language.learning.exercise;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum KnowledgeLevel {

	@XmlEnumValue(value = "BEGINNER") BEGINNER,
	@XmlEnumValue(value = "INTERMEDIATE") INTERMEDIATE, 
	@XmlEnumValue(value = "EXPERT") EXPERT;
	
}
