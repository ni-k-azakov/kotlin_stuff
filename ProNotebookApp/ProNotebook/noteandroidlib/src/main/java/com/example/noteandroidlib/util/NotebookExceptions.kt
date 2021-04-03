package util

import java.lang.Exception

class IncorrectDateFormat(incorrectDate: String) : Exception("Correct date format: DD.MM.YY ; " +
        "Format given: $incorrectDate")
class IncorrectTimeFormat(incorrectTime: String) : Exception("Correct date format: HH:MM (max 23:59); " +
        "Format given: $incorrectTime")
class UnknownSectionName(sectionName: String) : Exception("Section name: $sectionName")
class SectionNameTaken : Exception("Section with such name is already exists. Choose another name")