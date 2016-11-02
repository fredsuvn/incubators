package com.cogician.quicker.util.lexer;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-06T16:56:36+08:00
 * @since 0.0.0, 2016-04-06T16:56:36+08:00
 */
public class TestLexer {

    public static void main(String[] args) {
    	Pattern p = Pattern.compile("^((?!abc).)*");
    	Matcher m = p.matcher("aaa\r\naaabc");
    	System.out.println(m.lookingAt());
    	System.out.println(m.end());
//    	String str = "/**\r\n\ras\ngdgds*/function\n abc //ddddd\rfsfafasf";
//    	for (int i = 0; i < 2; i++){
//    		str += str;
//    	}
//        QuickLexer parser = new RegularLexerBuilder().addTokens(RegularLexToken.COMMENT
//                ,RegularLexToken.LINE_COMMENT,RegularLexToken.BLOCK_COMMENT
//                ,RegularLexToken.IDENTIFIER,RegularLexToken.SPACE,RegularLexToken.CRLF)
//                .build();
//        List<LexTetrad> list = parser.tokenize(str);
//        list.forEach(l -> {
//            System.out.println(l.toString());
//        });
//    	
//    	
//        
//        QuickLexer parser1 = new RegularLexerBuilder().addTokens(RegularLexToken.COMMENT
//                ,RegularLexToken.LINE_COMMENT,RegularLexToken.BLOCK_COMMENT
//                ,RegularLexToken.IDENTIFIER,RegularLexToken.SPACE,RegularLexToken.CRLF)
//                .build();
//        long l = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++){
//        	parser1.tokenize(str);
//        }
//        long c = System.currentTimeMillis() - l;
//        System.out.println(c);
        
//        Lexer parser2 = new RegularLexerBuilder().addTokens(RegularLexToken.COMMENT
//                ,RegularLexToken.LINE_COMMENT,RegularLexToken.BLOCK_COMMENT
//                ,RegularLexToken.IDENTIFIER,RegularLexToken.SPACE,RegularLexToken.CRLF)
//                .buildEach();
//        l = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++){
//        	parser2.tokenize(str);
//        }
//        c = System.currentTimeMillis() - l;
//        System.out.println(c);
    }

}
