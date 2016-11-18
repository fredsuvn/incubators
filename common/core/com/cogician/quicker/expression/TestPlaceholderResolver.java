package com.cogician.quicker.expression;

import java.util.HashMap;
import java.util.Map;

import com.cogician.quicker.expression.QuickExpressionParser.Result;
import com.cogician.quicker.util.QuickCollections;

/**
 * <p>
 * 
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-03T23:04:40+08:00
 * @since 0.0.0, 2016-08-03T23:04:40+08:00
 */
public class TestPlaceholderResolver {

    public static void main(String[] args) {
        QuickExpressionParser nResolver = QuickExpressionParser.defaultResolver();
        String nExp = "this is a \\${100\\}${0${2}}${}<[ tt${}tt <[dddd${}]>]>xxxxx<[ ${4}? \\${4\\}?]>";
        System.out.println(nResolver.resolve(nExp, "0", "2", "1", "4"));
        Result result = nResolver.resolveDetail(nExp, "0", "2", "1", "4");
        System.out.println(result.getUsedArguments());

        QuickExpressionParser uResolver = QuickExpressionParser.defaultSQLResolver();
        String uExp = "this is a \\:100:0:2:gg<[ ttt:t <[dddd:]>]>xxxxx<[ :0? \\${4\\}]>";
        System.out.println(uResolver.resolve(uExp, "0", "2", "1", "4"));
        result = uResolver.resolveDetail(uExp, "0", "2", "1", "4");
        System.out.println(result.getUsedArguments());

        String sql = "select * from table t where 1 = 1\r\n" + "<[ and t.id = :id\r\n]>" + "<[ and t.name = :name\r\n]>"
                + "<[ and t.group = :group\r\n]>" + " order by date\r\n" + "<[:id? ,id\r\n]>" + "<[:name? ,name\r\n]>"
                + "<[:group? ,group\r\n]>(xxxxx)";
        Map<String, String> map = new HashMap<>();
        map.put("id", "1004");
        map.put("group", "1");
        System.out.println(uResolver.resolve(sql, map));
        System.out.println(uResolver.resolveDetail(sql, map).getUsedArguments());

        Map<String, String> m = QuickCollections.multiKeysSingletonValueMap(map.keySet(), "?");
        result = uResolver.resolveDetail(sql, m);
        System.out.println(result.getResultString());

    }
}
