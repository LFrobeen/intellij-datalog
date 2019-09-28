package com.lfrobeen.datalog.lang.parser


import com.intellij.lang.*
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.tree.*
import com.lfrobeen.datalog.lang.DatalogLanguage
import com.lfrobeen.datalog.lang.lexer.DatalogLexer
import com.lfrobeen.datalog.lang.psi.DatalogElementType
import com.lfrobeen.datalog.lang.psi.DatalogPsiFile
import com.lfrobeen.datalog.lang.psi.DatalogTypes

class DatalogParserDefinition : ParserDefinition {

    override fun createLexer(project: Project): Lexer = DatalogLexer()

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRING_LITERALS

    override fun createParser(project: Project): PsiParser = DatalogParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return DatalogPsiFile(viewProvider)
    }

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

    override fun createElement(node: ASTNode): PsiElement {
        return DatalogTypes.Factory.createElement(node)
    }

    companion object {
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
        val STRING_LITERALS = TokenSet.create(DatalogTypes.STRING)
        val COMMENTS = TokenSet.create(
            DatalogTypes.COMMENT,
            DatalogElementType.DOC_COMMENT,
            DatalogElementType.EXT_COMMENT
        )

        val FILE = IFileElementType(DatalogLanguage)
    }
}