package data;

import util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * This class post-processes the raw excel data to extract needed features.
 */
public class PostProcess {

    /**
     * This method calculates all the derivated columns
     *
     * @param data the excel data.
     */
    public static void postProcess(List<Vector> data) {


        Vector b, c, d, e, f, g, h, i, j, k, l, m, n, o, p;
        Vector q, r, s, t, u, v, w, x, y, z, aa;
        Vector q0, q1, q2, q3, k0, l0, m0, n0, o0, ed1, cd1, dd1;
        Vector r0, r1, r2, r3, temp0, temp1, temp2, temp3, temp4, temp5, ca1, da1, ca2, da2;
        b = data.get(1);  // open
        c = data.get(2);  // max
        d = data.get(3);  // min
        e = data.get(4);  // close
        f = data.get(5);  // volume

        ed1 = e.shift(-1);   // close[-1]
        cd1 = c.shift(-1);   // max[-1]
        ca1 = c.shift(1);  // max[+1]
        ca2 = c.shift(2);  // max[+2]
        dd1 = d.shift(-1);   // min[-1]
        da1 = d.shift(1);  // min[+1]
        da2 = d.shift(2);  // min[+2]

        g = e.sub(b);                               // close - open
        h = b.max(e).scaleAddi(-1, c);              // max - max(open, close)
        i = b.min(e).subi(d);                       // min(open, close) - min
        j = c.sub(d);                               // max - min
        k0 = b.sub(ed1);                            // open - close[-1]
        l0 = g;
        m0 = e.sub(ed1);                            // close - close[-1]
        n0 = f.sub(f.shift(-1));                    // volume - volume[-1]
        o0 = b.shift(1).sub(e);                     // open[+1] - close
        k = Util.categorize(k0);
        l = Util.categorize(l0);
        m = Util.categorize(m0);
        n = Util.categorize(n0);
        o = Util.categorize(o0);
        // p = 'pv' if p[i] < 0 else ''
        p = m.mult(n).applyi(new Op() {
            @Override
            public double apply(double val) {
                return val < 0 ? 1 : 0;
            }
        });

        q0 = d.gt(cd1);                       // min > max[-1]
        q1 = d.gt(ed1);                       // min > close[-1]
        q2 = dd1.gt(c);                       // min[-1] > max
        q3 = ed1.gt(c);                       // close[-1] > max
        // q = DY if q0, LY if q1, DG if q2, LG if q3
        q = Vector.cond(q0, q1, q2, q3);

        temp0 = c.gt(cd1);
        temp1 = d.gt(dd1);
        temp2 = temp0.not();
        temp3 = temp1.not();
        r0 = temp0.mult(temp1);             // max > max[-1], min > min[-1]
        r1 = temp2.mult(temp3);             // max < max[-1], min < min[-1]
        r2 = temp2.mult(temp1);             // max < max[-1], min > min[-1]
        r3 = temp0.mult(temp3);             // max > max[-1], min < min[-1]
        // r = HH if r0, LL if r1, GS if r2, BU if r3
        r = Vector.cond(r0, r1, r2, r3);


        s = e.sub(ca1);                         // close - max[+1]
        t = e.sub(da1);                         // close - min[+1]
        u = e.sub(ca2);                         // close - max[+2]
        v = e.sub(da2);                         // close - min[+2]

        temp0 = s.add(t).gt(0);
        temp1 = u.add(v).gt(0);
        temp2 = temp0.not();
        temp3 = temp1.not();
        w = Vector.cond(temp2.mult(temp3), temp0.mult(temp1), temp2.mult(temp1), temp0.mult(temp3));
        // w = B if !temp0 && !temp1, S if temp0 && temp1, V if !temp0 && temp1, L if temp0 && !temp1

        Vector eint = e.integral();
        x = Util.movingAverage(eint, 5);
        y = Util.movingAverage(eint, 10);
        z = Util.movingAverage(eint, 125);
        aa = Util.movingAverage(eint, 250);

        data.add(g);
        data.add(h);
        data.add(i);
        data.add(j);
        data.add(k);
        data.add(l);
        data.add(m);
        data.add(n);
        data.add(o);
        data.add(p);
        data.add(q);
        data.add(r);
        data.add(s);
        data.add(t);
        data.add(u);
        data.add(v);
        data.add(w);
        data.add(x);
        data.add(y);
        data.add(z);
        data.add(aa);
    }

    /**
     * Convert column-format data to row-format output.
     * @param data the data
     * @return the output.
     */
    public static List<List<Value>> toOutput(List<Vector> data) {
        int n = data.get(0).size();
        List<List<Value>> ans = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            List<Value> row = new ArrayList<>(data.size());
            for (int j = 0; j < data.size(); j++) {
                row.add(Value.format(data.get(j).get(i), j));
            }
            ans.add(row);
        }
        return ans;

    }

}