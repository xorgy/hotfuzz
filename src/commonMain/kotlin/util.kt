expect fun normalizeNFD(s: String) : String;

fun mkgrams (s: String, depth: Int = 6) : Set<String> {
    var gs = HashSet<String>();
    var norm = normalizeNFD(s).codePointArrayList();
    var len = norm.count();
    for (i in 0.until(len)) {
        var outy = StringBuilder();
        for (j in 0.until(kotlin.math.min(depth, len - i))) {
            appendCodepoint(outy, norm[i + j]);
            gs.add(outy.toString());
        }
    }

    return gs;
}

fun boundWrap(s: String) : String {
    return "\u0002" + s + "\u0003";
}

private fun toCodePoint(high: Char, low: Char) : Int {
    return ((high.toInt() - 0xD800) * 0x400) + (low.toInt() - 0xDC00) + 0x10000;
}

private fun appendCodepoint(s: StringBuilder, c: Int) {
    if  (c in 0..0x10000) {
        s.append(c.toChar());
    } else if (c in 0x10000..0x10FFFF) {
        val low = ((c - 0x10000) and 0x3FF) + Char.MIN_LOW_SURROGATE.toInt();
        val high = (((c - 0x10000) ushr 10) and 0x3FF) + Char.MIN_HIGH_SURROGATE.toInt();
        s.append(high.toChar());
        s.append(low.toChar());
    }
}

private fun CharSequence.codePointArrayList(): ArrayList<Int> {
    var ret = ArrayList<Int>();
    var rem = this.asSequence();
    while (rem.iterator().hasNext()) {
        var first = rem.first();
        if (first.isHighSurrogate()) {
            var second = rem.drop(1).first();
            if (second.isLowSurrogate()) {
                rem = rem.drop(2);
                ret.add(toCodePoint(first, second));
            }
        }
        rem = rem.drop(1);
        ret.add(first.toInt());
    }
    return ret;
}
