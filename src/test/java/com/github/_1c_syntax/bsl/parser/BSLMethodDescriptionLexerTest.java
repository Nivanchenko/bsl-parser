/*
 * This file is a part of BSL Parser.
 *
 * Copyright © 2018-2020
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com>, Sergey Batanov <sergey.batanov@dmpas.ru>
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Parser is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Parser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Parser.
 */
package com.github._1c_syntax.bsl.parser;

import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BSLMethodDescriptionLexerTest extends AbstractLexerTest<BSLMethodDescriptionLexer> {

  BSLMethodDescriptionLexerTest() {
    super(BSLMethodDescriptionLexer.class);
  }

  @Test
  void testWhitespaces() {
    String inputString = "//   А";

    List<Token> tokens = getTokens(BSLMethodDescriptionLexer.DEFAULT_MODE, inputString);

    assertThat(tokens).extracting(Token::getType).containsExactly(
      BSLMethodDescriptionLexer.COMMENT,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.EOF
    );
  }

  @Test
  void testBOM() {
    assertMatch('\uFEFF' + "Процедура", BSLMethodDescriptionLexer.WORD);
  }

  @Test
  void testHyperlink() {
    assertMatch("СМ", BSLMethodDescriptionLexer.WORD);
    assertMatch("СМСМ", BSLMethodDescriptionLexer.WORD);
    assertMatch("SEE", BSLMethodDescriptionLexer.WORD);
    assertMatch("СМ.", BSLMethodDescriptionLexer.WORD, BSLMethodDescriptionLexer.ANYSYMBOL);
    assertMatch("СМ. ОбщийМодуль", BSLMethodDescriptionLexer.HYPERLINK);
    assertMatch("SEE ОбщийМодуль", BSLMethodDescriptionLexer.HYPERLINK);
    assertMatch("SEE  ОбщийМодуль",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD);
    assertMatch("SSEE ОбщийМодуль",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD);
    assertMatch("СМ. ОбщийМодуль.Метод", BSLMethodDescriptionLexer.HYPERLINK);
    assertMatch("SEE ОбщийМодуль.Метод", BSLMethodDescriptionLexer.HYPERLINK);
    assertMatch("SEE ОбщийМодуль.Метод()", BSLMethodDescriptionLexer.HYPERLINK);
    assertMatch("СМ. ОбщийМодуль.Метод(Параметра, Значение, \"\t+-'sdsds\")", BSLMethodDescriptionLexer.HYPERLINK);
    assertMatch("SEE ОбщийМодуль.Метод() WORD",
      BSLMethodDescriptionLexer.HYPERLINK,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD);
    assertMatch("SEE. ОбщийМодуль.Метод() WORD",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.ANYSYMBOL,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.DOTSWORD,
      BSLMethodDescriptionLexer.ANYSYMBOL,
      BSLMethodDescriptionLexer.ANYSYMBOL,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD);
    assertMatch("СМ.   ОбщийМодуль.Метод() WORD",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.ANYSYMBOL,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.DOTSWORD,
      BSLMethodDescriptionLexer.ANYSYMBOL,
      BSLMethodDescriptionLexer.ANYSYMBOL,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD);
  }

  @Test
  void testParameters() {
    assertMatch("Параметры", BSLMethodDescriptionLexer.WORD);
    assertMatch("Parameters", BSLMethodDescriptionLexer.WORD);
    assertMatch("NoParameters:", BSLMethodDescriptionLexer.WORD, BSLMethodDescriptionLexer.COLON);
    assertMatch("Параметры:", BSLMethodDescriptionLexer.PARAMETERS_KEYWORD);
    assertMatch("Parameters:", BSLMethodDescriptionLexer.PARAMETERS_KEYWORD);
    assertMatch("Параметры :", BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE, BSLMethodDescriptionLexer.COLON);
  }

  @Test
  void testReturns() {
    assertMatch("Возвращаемое значение",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD);
    assertMatch("RETURNS", BSLMethodDescriptionLexer.WORD);
    assertMatch("Возвращаемое  значение:",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.COLON);
    assertMatch("RETURNS :",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.COLON);
    assertMatch("Возвращаемое значение:", BSLMethodDescriptionLexer.RETURNS_KEYWORD);
    assertMatch("RETURNS:", BSLMethodDescriptionLexer.RETURNS_KEYWORD);
    assertMatch("НеВозвращаемое значение:",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.COLON);
    assertMatch("НЕRETURNS:",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.COLON);
  }

  @Test
  void testExample() {
    assertMatch("Пример", BSLMethodDescriptionLexer.WORD);
    assertMatch("ПримерЫ", BSLMethodDescriptionLexer.WORD);
    assertMatch("Example", BSLMethodDescriptionLexer.WORD);
    assertMatch("Examples", BSLMethodDescriptionLexer.WORD);
    assertMatch("Примеры:", BSLMethodDescriptionLexer.WORD, BSLMethodDescriptionLexer.COLON);
    assertMatch("Examples:", BSLMethodDescriptionLexer.WORD, BSLMethodDescriptionLexer.COLON);
    assertMatch("Пример:", BSLMethodDescriptionLexer.EXAMPLE_KEYWORD);
    assertMatch("Example:", BSLMethodDescriptionLexer.EXAMPLE_KEYWORD);
    assertMatch("Пример :",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.COLON);
    assertMatch("NoExample:",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.COLON);
  }

  @Test
  void testCallOptions() {
    assertMatch("Варианты вызова",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD);
    assertMatch("Call options",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD);
    assertMatch("Варианты  вызова:",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.COLON);
    assertMatch("Call options :",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.COLON);
    assertMatch("Варианты вызова:", BSLMethodDescriptionLexer.CALL_OPTIONS_KEYWORD);
    assertMatch("Call options:", BSLMethodDescriptionLexer.CALL_OPTIONS_KEYWORD);
    assertMatch("Вариант вызова:",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.COLON);
    assertMatch("Call option:",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.COLON);
  }

  @Test
  void testDeprecate() {
    assertMatch("Устарела", BSLMethodDescriptionLexer.WORD);
    assertMatch("Deprecate", BSLMethodDescriptionLexer.WORD);
    assertMatch("Depricate", BSLMethodDescriptionLexer.WORD);
    assertMatch("Устарела.", BSLMethodDescriptionLexer.DEPRECATE_KEYWORD);
    assertMatch("Deprecate.", BSLMethodDescriptionLexer.DEPRECATE_KEYWORD);
    assertMatch("Устарела .",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.SPACE,
      BSLMethodDescriptionLexer.ANYSYMBOL);
    assertMatch("Deprecate:",
      BSLMethodDescriptionLexer.WORD,
      BSLMethodDescriptionLexer.COLON);
  }
}
