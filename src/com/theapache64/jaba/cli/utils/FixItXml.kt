package com.theapache64.jaba.cli.utils

import java.io.File

object FixItXml {

    const val XML_CODE_STYLE_SETTINGS = """<AndroidXmlCodeStyleSettings>
      <option name="USE_CUSTOM_SETTINGS" value="true" />
    </AndroidXmlCodeStyleSettings>"""
    const val CODE_STYLE_SETTINGS = """<XML>
      <option name="XML_KEEP_LINE_BREAKS" value="false" />
      <option name="XML_ALIGN_ATTRIBUTES" value="false" />
      <option name="XML_SPACE_INSIDE_EMPTY_TAG" value="true" />
    </XML>
    <codeStyleSettings language="XML">
      <option name="FORCE_REARRANGE_MODE" value="1" />
      <indentOptions>
        <option name="CONTINUATION_INDENT_SIZE" value="4" />
      </indentOptions>
      <arrangement>
        <rules>
          <section>
            <rule>
              <match>
                <AND>
                  <NAME>xmlns:android</NAME>
                  <XML_NAMESPACE>^${'$'}</XML_NAMESPACE>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <NAME>xmlns:.*</NAME>
                  <XML_NAMESPACE>^${'$'}</XML_NAMESPACE>
                </AND>
              </match>
              <order>BY_NAME</order>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <NAME>.*:id</NAME>
                  <XML_NAMESPACE>http://schemas.android.com/apk/res/android</XML_NAMESPACE>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <NAME>.*:name</NAME>
                  <XML_NAMESPACE>http://schemas.android.com/apk/res/android</XML_NAMESPACE>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <NAME>name</NAME>
                  <XML_NAMESPACE>^${'$'}</XML_NAMESPACE>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <NAME>style</NAME>
                  <XML_NAMESPACE>^${'$'}</XML_NAMESPACE>
                </AND>
              </match>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <NAME>.*</NAME>
                  <XML_NAMESPACE>^${'$'}</XML_NAMESPACE>
                </AND>
              </match>
              <order>BY_NAME</order>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <NAME>.*</NAME>
                  <XML_NAMESPACE>http://schemas.android.com/apk/res/android</XML_NAMESPACE>
                </AND>
              </match>
              <order>ANDROID_ATTRIBUTE_ORDER</order>
            </rule>
          </section>
          <section>
            <rule>
              <match>
                <AND>
                  <NAME>.*</NAME>
                  <XML_NAMESPACE>.*</XML_NAMESPACE>
                </AND>
              </match>
              <order>BY_NAME</order>
            </rule>
          </section>
        </rules>
      </arrangement>
    </codeStyleSettings>"""
    const val CODE_SCHEME_NODE = "<code_scheme name=\"Project\" version=\"173\">"

    fun fixIt(projectDir: String) {

        // Building path to the codeStyle file
        val codeStyleFile = File("$projectDir/.idea/codeStyles/Project.xml")

        // Checking if the built method is correct by checking it's existence
        if (codeStyleFile.exists()) {

            val fileContent = codeStyleFile.readText()

            // Checking if the file is already modified
            if (fileContent.indexOf(XML_CODE_STYLE_SETTINGS) == -1) {

                val editList = fileContent.split(CODE_SCHEME_NODE).toMutableList()
                editList.add(1, CODE_SCHEME_NODE)
                editList.add(2, "\n\t$XML_CODE_STYLE_SETTINGS")
                editList.add(3, "\n\t$CODE_STYLE_SETTINGS")

                val output = editList.joinToString(separator = "")

                // Removing existing file
                if (codeStyleFile.delete()) {

                    // Creating new file with new content
                    if (codeStyleFile.createNewFile()) {
                        codeStyleFile.writeText(output)
                    } else {
                        printError("Failed to create new style file")
                    }

                } else {
                    printError("Failed to delete existing style file")
                }

            } else {
                printError("Can't edit style file. Another edit found.")
            }

        } else {
            printError("codeStyle file missing, might not be an IDEA project.")
        }
    }

    private fun printError(message: String) {
        println("ERROR : $message")
    }
}