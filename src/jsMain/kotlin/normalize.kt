actual fun normalizeNFD (s: String) : String {
    return js("s.normalize('NFD')").toString();
};