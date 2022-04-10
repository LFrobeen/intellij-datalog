package com.lfrobeen.datalog.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.FlexAdapter;
import com.intellij.psi.tree.IElementType;

import com.lfrobeen.datalog.lang.psi.DatalogTypes;

import static com.intellij.psi.TokenType.*;
import static com.lfrobeen.datalog.lang.psi.DatalogTypes.*;
import static com.lfrobeen.datalog.lang.psi.DatalogElementType.*;

%%

%{
  public _DatalogLexer() {
    this(null);
  }
 public static FlexAdapter getAdapter() {
   return new FlexAdapter(new _DatalogLexer());
 }
%}

%public
%class _DatalogLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

%state MACRO_NAME
%state MACRO_DECL

LINE_BREAK = [\r|\n|\r\n]

WHITE_SPACE=[ \t\f\r\n]+
WHITE_SPACE_NON_BREAKING=[ \t\f]+

STRING=\"[^\"]*\"
NUMBER_BIN=0b[0-1]+
NUMBER_HEX=0x[a-fA-F0-9]+
NUMBER_DEC=0|([1-9][0-9]*)

COMMENT=("//".*) | ("/**/") | ("/*" [^*] ~ "*/")

// Based on _JavaLexer.flex:
DOC_COMMENT="/*""*"+("/"|([^"/""*"]{COMMENT_TAIL}))?
COMMENT_TAIL=([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?

IDENTIFIER=[_?a-zA-Z][_?a-zA-Z0-9]+|[?a-zA-Z]

MACRO_CONTINUATION = \\[ \t\f]*{LINE_BREAK}

%%
<YYINITIAL> {
  {WHITE_SPACE}         { return WHITE_SPACE; }

  "#include"            { return INCLUDE_DIRECTIVE; }
  "#ifdef"              { return IFDEF_DIRECTIVE; }
  "#ifndef"             { return IFNDEF_DIRECTIVE; }
  "#elif"               { return ELIF_DIRECTIVE; }
  "#else"               { return ELSE_DIRECTIVE; }
  "#endif"              { return ENDIF_DIRECTIVE; }
  "#define"             { yybegin(MACRO_DECL); return DEFINE_DIRECTIVE; }
}

<YYINITIAL> {
  "float"               { return FLOAT_TYPE; }
  "number"              { return NUMBER_TYPE; }
  "unsigned"            { return UNSIGNED_TYPE; }
  "symbol"              { return SYMBOL_TYPE; }
  "nil"                 { return NIL; }
  ".comp"               { return COMP_DIRECTIVE; }
  ".decl"               { return RELATION_DIRECTIVE; }
  ".type"               { return TYPE_DIRECTIVE; }
  ".symbol_type"        { return TYPE_SYM_DIRECTIVE; }
  ".number_type"        { return TYPE_NUM_DIRECTIVE; }
  ".init"               { return INIT_DIRECTIVE; }
  ".input"              { return INPUT_DIRECTIVE; }
  ".output"             { return OUTPUT_DIRECTIVE; }
  ".printsize"          { return PRINTSIZE_DIRECTIVE; }
  ".override"           { return OVERRIDE_DIRECTIVE; }
  ".pragma"             { return PRAGMA_DIRECTIVE; }
  ".functor"            { return FUNCTOR_DIRECTIVE; }

  "output"              { return OUTPUT_QUALIFIER; }
  "input"               { return INPUT_QUALIFIER; }
  "printsize"           { return PRINTSIZE_QUALIFIER; }
  "overridable"         { return OVERRIDABLE_QUALIFIER; }
  "inline"              { return INLINE_QUALIFIER; }
  "brie"                { return BRIE_QUALIFIER; }
  "btree"               { return BTREE_QUALIFIER; }
  "eqrel"               { return EQREL_QUALIFIER; }

  "_"                   { return UNDERSCORE; }
  "$"                   { return DOLLAR; }

  "count"               { return COUNT; }
  "mean"                { return MEAN; }
  "max"                 { return MAX; }
  "min"                 { return MIN; }
  "sum"                 { return SUM; }

  "lnot"                { return LNOT; }
  "lor"                 { return LOR; }
  "land"                { return LAND; }
  "bnot"                { return BNOT; }
  "band"                { return BAND; }
  "bxor"                { return BXOR; }
  "bor"                 { return BOR; }

  "as"                  { return AS; }

  "true"                { return DatalogTypes.TRUE; }
  "false"               { return DatalogTypes.FALSE; }

  "@"                   { return AT; }

  "|"                   { return PIPE; }
  "."                   { return DOT; }
  "!"                   { return NOT; }
  ":-"                  { return IF; }
  ","                   { return COMMA; }
  ":"                   { return COLON; }
  ";"                   { return SEMICOLON; }
  "("                   { return LPARENTH; }
  ")"                   { return RPARENTH; }
  "["                   { return LBRACKET; }
  "]"                   { return RBRACKET; }
  "{"                   { return LBRACE; }
  "}"                   { return RBRACE; }

  "*"                   { return MUL; }
  "/"                   { return RDIV; }
  "+"                   { return PLUS; }
  "-"                   { return MINUS; }
  "%"                   { return MOD; }
  "^"                   { return POW; }
  "="                   { return EQUAL; }
  "!="                  { return NOT_EQUAL; }
  "<"                   { return LESS; }
  "<="                  { return LESS_OR_EQUAL; }
  ">"                   { return MORE; }
  ">="                  { return MORE_OR_EQUAL; }

  {STRING}              { return STRING; }
  {NUMBER_BIN}          { return NUMBER_BIN; }
  {NUMBER_HEX}          { return NUMBER_HEX; }
  {NUMBER_DEC}          { return NUMBER_DEC; }
  {IDENTIFIER}          { return IDENTIFIER; }
  {COMMENT}             { return COMMENT; }
  {DOC_COMMENT}         { return COMMENT; }
}

<MACRO_DECL> {
  {WHITE_SPACE_NON_BREAKING} { return WHITE_SPACE; }
  {MACRO_CONTINUATION}  { return MACRO_CONTINUATION; }
  {LINE_BREAK}          { yybegin(YYINITIAL); return LINE_BREAK; }

  {STRING}              { return STRING; }
  {NUMBER_BIN}          { return NUMBER_BIN; }
  {NUMBER_HEX}          { return NUMBER_HEX; }
  {NUMBER_DEC}          { return NUMBER_DEC; }
  {COMMENT}             { return COMMENT; }
  {IDENTIFIER}          { return IDENTIFIER; }

  [^]                   { return MACRO_TOKEN; }
}

[^] { return BAD_CHARACTER; }
