/*
 * Yet Another UserAgent Analyzer
 * Copyright (C) 2013-2016 Niels Basjes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.basjes.parse.useragent.analyze.treewalker.steps.value;

import nl.basjes.parse.useragent.UserAgentParser.SingleVersionContext;
import nl.basjes.parse.useragent.UserAgentParser.SingleVersionWithCommasContext;
import nl.basjes.parse.useragent.analyze.WordRangeVisitor;
import nl.basjes.parse.useragent.analyze.treewalker.steps.Step;
import nl.basjes.parse.useragent.utils.VersionSplitter;
import nl.basjes.parse.useragent.utils.WordSplitter;
import org.antlr.v4.runtime.tree.ParseTree;

public class StepWordRange extends Step {

    private final int firstWord;
    private final int lastWord;

    public StepWordRange(WordRangeVisitor.Range range) {
        this.firstWord = range.getFirst();
        this.lastWord = range.getLast();
    }

    @Override
    public String walk(ParseTree tree, String value) {
        String actualValue = getActualValue(tree, value);
        String filteredValue;
        if (tree.getChildCount() == 1 && (
              tree.getChild(0) instanceof SingleVersionContext |
              tree.getChild(0) instanceof SingleVersionWithCommasContext)) {
            filteredValue = VersionSplitter.getVersionRange(actualValue, firstWord, lastWord);
        } else {
            filteredValue = WordSplitter.getWordRange(actualValue, firstWord, lastWord);
        }
        if (filteredValue == null) {
            return null;
        }
        return walkNextStep(tree, filteredValue);
    }

    @Override
    public String toString() {
        return "WordRange(" + firstWord + "-" + lastWord + ")";
    }

}