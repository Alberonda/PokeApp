package com.example.pokeapp.base

fun <A, B> List<Pair<A, B>>.mergeByKeys(
    functionToApplyToValues: (B, B) -> B
): List<Pair<A, B>> {
    return this.groupBy {
        it.first
    }.mapValues { (_, relations) ->
        relations.reduce { acc, next ->
            Pair(
                acc.first,
                functionToApplyToValues(acc.second, next.second)
            )
        }
    }.values.toList()
}