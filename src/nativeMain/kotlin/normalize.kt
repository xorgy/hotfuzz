// FIXME: Normalization seems to be hard on Kotlin Native right now; oh well, fake it 'til you make it.
actual fun normalizeNFD (s: String) : String {
    return s;
};