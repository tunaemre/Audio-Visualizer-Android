package com.tunaemre.audiovisualizer.fft;

/**
 * FFT transform of a real periodic sequence.
 */
public class RealDoubleFFT extends BaseRealDoubleFFT {
    /**
     * <em>normalizeFactor</em> can be used to normalize this FFT transform. This is because
     * a call of forward transform (<em>forwardTransform</em>) followed by a call of backward transform
     * (<em>backwardTransform</em>) will multiply the input sequence by <em>normalizeFactor</em>.
     */
    public double normalizeFactor;
    private double wavetable[];
    private int nDimension;

    /**
     * Construct a wavenumber table with size <em>n</em>.
     * The sequences with the same size can share a wavenumber table. The prime
     * factorization of <em>n</em> together with a tabulation of the trigonometric functions
     * are computed and stored.
     *
     * @param n the size of a real data sequence. When <em>n</em> is a multiplication of small
     *          numbers (4, 2, 3, 5), this FFT transform is very efficient.
     */
    public RealDoubleFFT(int n) {
        nDimension = n;
        normalizeFactor = n;

        if (wavetable == null || wavetable.length != (2 * nDimension + 15))
            wavetable = new double[2 * nDimension + 15];

        rffti(nDimension, wavetable);
    }

    /**
     * Forward real FFT transform. It computes the discrete transform of a real data sequence.
     *
     * @param x an array which contains the sequence to be transformed. After FFT,
     *          <em>real</em> contains the transform coeffients used to construct <em>n</em> complex FFT coeffients.
     *          <br>
     *          The real part of the first complex FFT coeffients is <em>real</em>[0]; its imaginary part
     *          is 0. If <em>n</em> is even set <em>m</em> = <em>n</em>/2, if <em>n</em> is odd set
     *          <em>m</em> = <em>n</em>/2, then for
     *          <br>
     *          <em>k</em> = 1, ..., <em>m</em>-1 <br>
     *          the real part of <em>k</em>-th complex FFT coeffients is <em>real</em>[2*<em>k</em>-1];
     *          <br>
     *          the imaginary part of <em>k</em>-th complex FFT coeffients is <em>real</em>[2*<em>k</em>-2].
     *          <br>
     *          If <em>n</em> is even,
     *          the real of part of (<em>n</em>/2)-th complex FFT coeffients is <em>real</em>[<em>n</em>]; its imaginary part is 0.
     *          The remaining complex FFT coeffients can be obtained by the symmetry relation:
     *          the (<em>n</em>-<em>k</em>)-th complex FFT coeffient is the conjugate of <em>n</em>-th complex FFT coeffient.
     */
    public void forwardTransform(double x[]) {
        if (x.length != nDimension)
            throw new IllegalArgumentException("The length of data can not match that of the wavetable");

        rfftf(nDimension, x, wavetable);
    }

    /**
     * Backward real FFT transform. It is the unnormalized inverse transform of <em>forwardTransform</em>(double[]).
     *
     * @param x an array which contains the sequence to be transformed. After FFT,
     *          <em>real</em> contains the transform coeffients. Also see the comments of <em>forwardTransform</em>(double[])
     *          for the relation between <em>real</em> and complex FFT coeffients.
     */
    @Deprecated
    public void backwardTransform(double x[]) {
        if (x.length != nDimension)
            throw new IllegalArgumentException("The length of data can not match that of the wavetable");

        rfftb(nDimension, x, wavetable);
    }
}
