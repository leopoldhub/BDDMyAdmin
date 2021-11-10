package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.utils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ParameterUtils {

    public static Set<Map.Entry<String, String[]>> getColumnsEntries(Map<String, String[]> parametersMap){
        return getPrefixEntries(parametersMap, BDDUtils.COLUMN_PREFIX_IDENTIFIER);
    }

    public static Set<Map.Entry<String, String[]>> getOldColumnsEntries(Map<String, String[]> parametersMap){
        return getPrefixEntries(parametersMap, BDDUtils.OLD_COLUMN_PREFIX_IDENTIFIER+BDDUtils.COLUMN_PREFIX_IDENTIFIER);
    }

    public static Set<Map.Entry<String, String[]>> getPrefixEntries(Map<String, String[]> parametersMap, String prefix){
        return parametersMap.entrySet().stream()
                .filter(parameterEntry -> parameterEntry.getKey().startsWith(prefix))
                .map(parameterEntry -> new AbstractMap.SimpleEntry<>(
                        parameterEntry.getKey().substring(prefix.length()),
                        parameterEntry.getValue())
                )
                .collect(Collectors.toSet());
    }

}
