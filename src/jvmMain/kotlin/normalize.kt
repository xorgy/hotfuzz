actual fun normalizeNFD (s: String) : String {
    return java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
};