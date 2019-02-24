/*
  mj.lex
    Tokens/Symbols for MeggyJava language.
    NO dollars, but underscores anywhere in identifiers
    (to avoid problems later when generating AVR labels from id-s)
    8 colors (not all the CPP colors)
    The values for the colors, buttons, and tones were found in
    MeggyJrSimple.h and MeggyJrSimple.cpp.
    Ignore single line comments: // until eol
    ALSO: ignore C style comments, see http://ostermiller.org/findcomment.html
    Wim Bohm and Michelle Strout, 6/2011
*/
package mjparser;
import java_cup.runtime.Symbol;


public class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	public Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NOT_ACCEPT,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NOT_ACCEPT,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NOT_ACCEPT,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NOT_ACCEPT,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NOT_ACCEPT,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NOT_ACCEPT,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NOT_ACCEPT,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NOT_ACCEPT,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NOT_ACCEPT,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NOT_ACCEPT,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NOT_ACCEPT,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NOT_ACCEPT,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NOT_ACCEPT,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NOT_ACCEPT,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NOT_ACCEPT,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NOT_ACCEPT,
		/* 128 */ YY_NOT_ACCEPT,
		/* 129 */ YY_NOT_ACCEPT,
		/* 130 */ YY_NOT_ACCEPT,
		/* 131 */ YY_NOT_ACCEPT,
		/* 132 */ YY_NOT_ACCEPT,
		/* 133 */ YY_NOT_ACCEPT,
		/* 134 */ YY_NOT_ACCEPT,
		/* 135 */ YY_NOT_ACCEPT,
		/* 136 */ YY_NOT_ACCEPT,
		/* 137 */ YY_NOT_ACCEPT,
		/* 138 */ YY_NOT_ACCEPT,
		/* 139 */ YY_NOT_ACCEPT,
		/* 140 */ YY_NOT_ACCEPT,
		/* 141 */ YY_NOT_ACCEPT,
		/* 142 */ YY_NOT_ACCEPT,
		/* 143 */ YY_NOT_ACCEPT,
		/* 144 */ YY_NOT_ACCEPT,
		/* 145 */ YY_NOT_ACCEPT,
		/* 146 */ YY_NOT_ACCEPT,
		/* 147 */ YY_NOT_ACCEPT,
		/* 148 */ YY_NOT_ACCEPT,
		/* 149 */ YY_NOT_ACCEPT,
		/* 150 */ YY_NOT_ACCEPT,
		/* 151 */ YY_NOT_ACCEPT,
		/* 152 */ YY_NOT_ACCEPT,
		/* 153 */ YY_NOT_ACCEPT,
		/* 154 */ YY_NOT_ACCEPT,
		/* 155 */ YY_NOT_ACCEPT,
		/* 156 */ YY_NOT_ACCEPT,
		/* 157 */ YY_NOT_ACCEPT,
		/* 158 */ YY_NOT_ACCEPT,
		/* 159 */ YY_NOT_ACCEPT,
		/* 160 */ YY_NOT_ACCEPT,
		/* 161 */ YY_NOT_ACCEPT,
		/* 162 */ YY_NOT_ACCEPT,
		/* 163 */ YY_NOT_ACCEPT,
		/* 164 */ YY_NOT_ACCEPT,
		/* 165 */ YY_NOT_ACCEPT,
		/* 166 */ YY_NOT_ACCEPT,
		/* 167 */ YY_NOT_ACCEPT,
		/* 168 */ YY_NOT_ACCEPT,
		/* 169 */ YY_NOT_ACCEPT,
		/* 170 */ YY_NOT_ACCEPT,
		/* 171 */ YY_NOT_ACCEPT,
		/* 172 */ YY_NOT_ACCEPT,
		/* 173 */ YY_NOT_ACCEPT,
		/* 174 */ YY_NOT_ACCEPT,
		/* 175 */ YY_NOT_ACCEPT,
		/* 176 */ YY_NOT_ACCEPT,
		/* 177 */ YY_NOT_ACCEPT,
		/* 178 */ YY_NOT_ACCEPT,
		/* 179 */ YY_NOT_ACCEPT,
		/* 180 */ YY_NOT_ACCEPT,
		/* 181 */ YY_NOT_ACCEPT,
		/* 182 */ YY_NOT_ACCEPT,
		/* 183 */ YY_NOT_ACCEPT,
		/* 184 */ YY_NOT_ACCEPT,
		/* 185 */ YY_NOT_ACCEPT,
		/* 186 */ YY_NOT_ACCEPT,
		/* 187 */ YY_NOT_ACCEPT,
		/* 188 */ YY_NOT_ACCEPT,
		/* 189 */ YY_NOT_ACCEPT,
		/* 190 */ YY_NOT_ACCEPT,
		/* 191 */ YY_NOT_ACCEPT,
		/* 192 */ YY_NOT_ACCEPT,
		/* 193 */ YY_NOT_ACCEPT,
		/* 194 */ YY_NOT_ACCEPT,
		/* 195 */ YY_NOT_ACCEPT,
		/* 196 */ YY_NOT_ACCEPT,
		/* 197 */ YY_NOT_ACCEPT,
		/* 198 */ YY_NO_ANCHOR,
		/* 199 */ YY_NOT_ACCEPT,
		/* 200 */ YY_NOT_ACCEPT,
		/* 201 */ YY_NOT_ACCEPT,
		/* 202 */ YY_NOT_ACCEPT,
		/* 203 */ YY_NOT_ACCEPT,
		/* 204 */ YY_NOT_ACCEPT,
		/* 205 */ YY_NOT_ACCEPT,
		/* 206 */ YY_NOT_ACCEPT,
		/* 207 */ YY_NOT_ACCEPT,
		/* 208 */ YY_NOT_ACCEPT,
		/* 209 */ YY_NOT_ACCEPT,
		/* 210 */ YY_NOT_ACCEPT,
		/* 211 */ YY_NOT_ACCEPT,
		/* 212 */ YY_NOT_ACCEPT,
		/* 213 */ YY_NOT_ACCEPT,
		/* 214 */ YY_NOT_ACCEPT,
		/* 215 */ YY_NOT_ACCEPT,
		/* 216 */ YY_NO_ANCHOR,
		/* 217 */ YY_NOT_ACCEPT,
		/* 218 */ YY_NOT_ACCEPT,
		/* 219 */ YY_NOT_ACCEPT,
		/* 220 */ YY_NOT_ACCEPT,
		/* 221 */ YY_NOT_ACCEPT,
		/* 222 */ YY_NOT_ACCEPT,
		/* 223 */ YY_NO_ANCHOR,
		/* 224 */ YY_NOT_ACCEPT,
		/* 225 */ YY_NO_ANCHOR,
		/* 226 */ YY_NO_ANCHOR,
		/* 227 */ YY_NO_ANCHOR,
		/* 228 */ YY_NO_ANCHOR,
		/* 229 */ YY_NO_ANCHOR,
		/* 230 */ YY_NO_ANCHOR,
		/* 231 */ YY_NO_ANCHOR,
		/* 232 */ YY_NO_ANCHOR,
		/* 233 */ YY_NO_ANCHOR,
		/* 234 */ YY_NO_ANCHOR,
		/* 235 */ YY_NO_ANCHOR,
		/* 236 */ YY_NO_ANCHOR,
		/* 237 */ YY_NO_ANCHOR,
		/* 238 */ YY_NO_ANCHOR,
		/* 239 */ YY_NO_ANCHOR,
		/* 240 */ YY_NO_ANCHOR,
		/* 241 */ YY_NO_ANCHOR,
		/* 242 */ YY_NO_ANCHOR,
		/* 243 */ YY_NO_ANCHOR,
		/* 244 */ YY_NO_ANCHOR,
		/* 245 */ YY_NO_ANCHOR,
		/* 246 */ YY_NO_ANCHOR,
		/* 247 */ YY_NO_ANCHOR,
		/* 248 */ YY_NO_ANCHOR,
		/* 249 */ YY_NO_ANCHOR,
		/* 250 */ YY_NO_ANCHOR,
		/* 251 */ YY_NO_ANCHOR,
		/* 252 */ YY_NO_ANCHOR,
		/* 253 */ YY_NO_ANCHOR,
		/* 254 */ YY_NO_ANCHOR,
		/* 255 */ YY_NO_ANCHOR,
		/* 256 */ YY_NO_ANCHOR,
		/* 257 */ YY_NO_ANCHOR,
		/* 258 */ YY_NO_ANCHOR,
		/* 259 */ YY_NO_ANCHOR,
		/* 260 */ YY_NOT_ACCEPT,
		/* 261 */ YY_NOT_ACCEPT,
		/* 262 */ YY_NO_ANCHOR,
		/* 263 */ YY_NO_ANCHOR,
		/* 264 */ YY_NO_ANCHOR,
		/* 265 */ YY_NO_ANCHOR,
		/* 266 */ YY_NO_ANCHOR,
		/* 267 */ YY_NO_ANCHOR,
		/* 268 */ YY_NO_ANCHOR,
		/* 269 */ YY_NO_ANCHOR,
		/* 270 */ YY_NO_ANCHOR,
		/* 271 */ YY_NO_ANCHOR,
		/* 272 */ YY_NO_ANCHOR,
		/* 273 */ YY_NO_ANCHOR,
		/* 274 */ YY_NO_ANCHOR,
		/* 275 */ YY_NO_ANCHOR,
		/* 276 */ YY_NO_ANCHOR,
		/* 277 */ YY_NO_ANCHOR,
		/* 278 */ YY_NO_ANCHOR,
		/* 279 */ YY_NO_ANCHOR,
		/* 280 */ YY_NO_ANCHOR,
		/* 281 */ YY_NO_ANCHOR,
		/* 282 */ YY_NO_ANCHOR,
		/* 283 */ YY_NOT_ACCEPT,
		/* 284 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"66:9,69,67,66,69,68,66:18,69,61,66:4,59,66,52,53,49,48,50,66,24,65,62:3,46," +
"62:6,66,51,60,58,66:3,27,32,33,30,29,47,38,45,43,63,35,28,25,37,36,26,63,34" +
",19,44,41,42,40,63,39,63,56,66,57,66,64,66,2,5,18,22,8,11,20,16,3,63,31,7,1" +
",4,6,17,63,13,12,9,14,21,15,23,10,63,54,66,55,66:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,285,
"0,1,2,1,3,1:10,4,1:4,5,1:2,6,5:8,1,5:11,7,1:2,8,9,1:31,10,11,12,13,14,15,16" +
",17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41" +
",42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66" +
",67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91" +
",92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,11" +
"2,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,1" +
"31,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149," +
"150,151,152,153,154,5,155,156,157,158,159,160,161,162,163,164,165,166,167,1" +
"68,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186," +
"187,5,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,2" +
"05,206,207,208,209,210,211,212")[0];

	private int yy_nxt[][] = unpackFromString(213,70,
"1,2,259,81,198,268,259,270,272,273,259,274,275,276,259,277,259,278,279,280," +
"259,281,259:2,3,282,259:20,4,259,5,6,7,8,9,10,11,12,13,14,15,82,16,17,4,259" +
":2,86,3,18,83,19,-1:71,259,216,259:5,223,259:15,-1,259:21,225,259,-1:14,225" +
",259,225,-1:51,4,-1:15,4,-1:65,21,-1:12,259:23,-1,259:21,225,259,-1:14,225," +
"259,225,-1:6,23:66,-1:2,23,-1:24,137,-1:69,142,-1:69,155,-1:46,80:48,84,80:" +
"20,-1,226,259:2,85,259:6,20,259:12,-1,259:21,225,259,-1:14,225,259,225,-1:6" +
"4,22,-1:77,18,-1:3,87:48,84,87:15,32,87:4,-1,259:8,24,259:14,-1,259:21,225," +
"259,-1:14,225,259,225,-1:54,80,-1:15,23,-1:5,87:48,84,87:20,-1,259:14,25,25" +
"9:8,-1,259:21,225,259,-1:14,225,259,225,-1:30,93,-1:45,259:3,26,259:19,-1,2" +
"59:21,225,259,-1:14,225,259,225,-1:14,95,-1:2,199,-1:5,97,-1,217,-1,224,-1:" +
"9,99,200,-1:10,260,-1:26,259:7,27,259:15,-1,259:21,225,259,-1:14,225,259,22" +
"5,-1:13,101,-1:62,259:7,28,259:15,-1,259:21,225,259,-1:14,225,259,225,-1:11" +
",103,-1:64,259:7,29,259:15,-1,259:21,225,259,-1:14,225,259,225,-1:21,107,-1" +
":54,259:11,30,259:11,-1,259:21,225,259,-1:14,225,259,225,-1:19,219,-1:56,25" +
"9:21,31,259,-1,259:21,225,259,-1:14,225,259,225,-1:25,201,-1:50,259:7,33,25" +
"9:15,-1,259:21,225,259,-1:14,225,259,225,-1:9,204,-1:66,259:7,34,259:15,-1," +
"259:21,225,259,-1:14,225,259,225,-1:14,111,-1:61,259:11,35,259:11,-1,259:21" +
",225,259,-1:14,225,259,225,-1:13,113,-1:62,259:23,89,259:21,225,259,-1:14,2" +
"25,259,225,-1:12,117,-1:63,259:8,36,259:14,-1,259:21,225,259,-1:14,225,259," +
"225,-1:31,128,129,-1:43,259:15,37,259:7,-1,259:21,225,259,-1:14,225,259,225" +
",-1:23,130,-1:52,259:17,38,259:5,-1,259:21,225,259,-1:14,225,259,225,-1:31," +
"283,-1:44,259:3,39,259:19,-1,259:21,225,259,-1:14,225,259,225,-1:7,131,-1:6" +
"8,259:17,40,259:5,-1,259:21,225,259,-1:14,225,259,225,-1:14,206,-1:61,259:1" +
"9,41,259:3,-1,259:21,225,259,-1:14,225,259,225,-1:11,132,-1:64,259:23,91,25" +
"9:21,225,259,-1:14,225,259,225,-1:13,44,-1:62,259:3,42,259:19,-1,259:21,225" +
",259,-1:14,225,259,225,-1:15,45,-1:60,259:11,43,259:11,-1,259:21,225,259,-1" +
":14,225,259,225,-1:24,133,-1:53,134,-1:80,207,-1:86,135,-1:48,46,-1:72,47,-" +
"1:65,138,-1:83,139,-1:78,141,-1:41,48,-1:92,143,-1,144,145,-1,146,147,-1:4," +
"148,-1:8,149,-1:24,150,-1:75,151,-1:89,152,-1:55,153,-1:85,156,-1,157,-1,20" +
"9,-1,158,-1,211,220,159,-1,160,-1:39,161,-1:33,49,-1:69,50,-1:35,162,-1:33," +
"51,-1:69,52,-1:35,163,-1:33,53,-1:35,164,-1:33,54,-1:35,165,-1:33,55,-1:36," +
"166,-1:63,56,-1:91,167,-1:49,210,-1:67,57,-1:89,58,168,-1,169,-1,59,-1,170," +
"-1:6,171,-1:55,221,-1:70,172,-1:75,174,-1:80,212,-1:67,177,-1:72,60,-1:69,6" +
"1,-1:69,62,-1:69,63,-1:69,64,-1:32,65,-1:90,178,-1:47,179,-1:67,180,-1:66,2" +
"18,-1:83,66,-1:93,182,-1:58,67,-1:66,183,-1:71,214,-1:68,215,-1:77,185,-1:4" +
"5,68,-1:68,187,-1:73,188,-1:89,69,-1:63,70,-1:77,190,-1:76,193,-1:53,222,-1" +
":45,71,-1:74,72,-1:64,73,-1:81,194,-1:91,195,-1:68,74,-1:68,196,-1:62,75,-1" +
":49,76,-1:89,77,-1:80,78,-1:73,79,-1:26,259:7,88,259:15,-1,259:21,225,259,-" +
"1:14,225,259,225,-1:13,105,-1:67,205,-1:83,125,-1:53,123,-1:74,115,-1:68,12" +
"7,-1:68,121,-1:68,136,-1:86,140,-1:54,154,-1:90,173,-1:49,213,-1:94,175,-1:" +
"78,184,-1:32,186,-1:92,191,-1:68,192,-1:42,259:2,90,259:20,-1,259:21,225,25" +
"9,-1:14,225,259,225,-1:13,203,-1:81,189,-1:58,119,-1:89,176,-1:74,181,-1:64" +
",197,-1:41,259:19,262,259:3,-1,259:21,225,259,-1:14,225,259,225,-1:13,109,-" +
"1:62,259:16,263,259:6,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:5,265,25" +
"9:17,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:8,92,259:14,-1,259:21,225" +
",259,-1:14,225,259,225,-1:6,259:3,269,259:19,-1,259:21,225,259,-1:14,225,25" +
"9,225,-1:6,259:11,94,259:11,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:8," +
"240,259:14,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:13,96,259:9,-1,259:" +
"21,225,259,-1:14,225,259,225,-1:6,259:2,98,259:20,-1,259:21,225,259,-1:14,2" +
"25,259,225,-1:6,259:6,241,259:16,-1,259:21,225,259,-1:14,225,259,225,-1:6,2" +
"59,242,259:21,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:2,244,259:20,-1," +
"259:21,225,259,-1:14,225,259,225,-1:6,259:4,245,259:18,-1,259:21,225,259,-1" +
":14,225,259,225,-1:6,259:12,247,259:10,-1,259:21,225,259,-1:14,225,259,225," +
"-1:6,259:2,100,259:20,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:7,251,25" +
"9:15,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:11,102,259:11,-1,259:21,2" +
"25,259,-1:14,225,259,225,-1:6,259:8,252,259:14,-1,259:21,225,259,-1:14,225," +
"259,225,-1:6,259:13,253,259:9,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:" +
"6,104,259:16,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:6,254,259:16,-1,2" +
"59:21,225,259,-1:14,225,259,225,-1:6,259:11,106,259:11,-1,259:21,225,259,-1" +
":14,225,259,225,-1:6,259:2,255,259:20,-1,259:21,225,259,-1:14,225,259,225,-" +
"1:6,259:9,108,259:13,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:12,110,25" +
"9:10,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:8,112,259:14,-1,259:21,22" +
"5,259,-1:14,225,259,225,-1:6,259:3,258,259:19,-1,259:21,225,259,-1:14,225,2" +
"59,225,-1:6,259:2,114,259:20,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:1" +
"2,116,259:10,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:2,118,259:20,-1,2" +
"59:21,225,259,-1:14,225,259,225,-1:6,259:3,120,259:19,-1,259:21,225,259,-1:" +
"14,225,259,225,-1:6,259:9,122,259:13,-1,259:21,225,259,-1:14,225,259,225,-1" +
":6,259,124,259:21,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:21,126,259,-" +
"1,259:21,225,259,-1:14,225,259,225,-1:11,202,-1:86,208,-1:47,259:19,248,259" +
":3,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:5,249,259:17,-1,259:21,225," +
"259,-1:14,225,259,225,-1:6,259:8,243,259:14,-1,259:21,225,259,-1:14,225,259" +
",225,-1:6,259:6,267,259:16,-1,259:21,225,259,-1:14,225,259,225,-1:6,259,246" +
",259:21,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:7,257,259:15,-1,259:21" +
",225,259,-1:14,225,259,225,-1:6,259:5,227,259:3,228,259:13,-1,259:21,225,25" +
"9,-1:14,225,259,225,-1:6,259:19,250,259:3,-1,259:21,225,259,-1:14,225,259,2" +
"25,-1:6,259:7,229,259:15,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:19,25" +
"6,259:3,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:6,230,259:15,231,-1,25" +
"9:21,225,259,-1:14,225,259,225,-1:6,259:12,232,259:2,233,259:7,-1,259:21,22" +
"5,259,-1:14,225,259,225,-1:6,259,234,259:21,-1,259:21,225,259,-1:14,225,259" +
",225,-1:6,259:8,235,259:14,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:7,2" +
"64,259:15,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:15,236,259:7,-1,259:" +
"21,225,259,-1:14,225,259,225,-1:6,259:13,237,259:9,-1,259:21,225,259,-1:14," +
"225,259,225,-1:6,259:6,266,259:16,-1,259:21,225,259,-1:14,225,259,225,-1:6," +
"259:8,238,259:14,-1,259:21,225,259,-1:14,225,259,225,-1:6,259:5,239,259:17," +
"-1,259:21,225,259,-1:14,225,259,225,-1:6,259:7,284,259:15,-1,259:21,225,259" +
",-1:14,225,259,225,-1:8,261,-1:67,259:19,271,259:3,-1,259:21,225,259,-1:14," +
"225,259,225,-1:5");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

  return new Symbol(sym.EOF, new SymbolValue(yyline, yychar+1, "EOF"));
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -3:
						break;
					case 3:
						{ System.err.println("Illegal character: "+yytext()); }
					case -4:
						break;
					case 4:
						{return new Symbol(sym.INT_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), new Integer(yytext())));}
					case -5:
						break;
					case 5:
						{return new Symbol(sym.PLUS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -6:
						break;
					case 6:
						{return new Symbol(sym.TIMES,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -7:
						break;
					case 7:
						{return new Symbol(sym.COMMA,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -8:
						break;
					case 8:
						{return new Symbol(sym.SEMI,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -9:
						break;
					case 9:
						{return new Symbol(sym.LPAREN,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -10:
						break;
					case 10:
						{return new Symbol(sym.RPAREN,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -11:
						break;
					case 11:
						{return new Symbol(sym.LBRACE,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -12:
						break;
					case 12:
						{return new Symbol(sym.RBRACE,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -13:
						break;
					case 13:
						{return new Symbol(sym.LBRACKET,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -14:
						break;
					case 14:
						{return new Symbol(sym.RBRACKET,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -15:
						break;
					case 15:
						{return new Symbol(sym.ASSIGN,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -16:
						break;
					case 16:
						{return new Symbol(sym.LT,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -17:
						break;
					case 17:
						{return new Symbol(sym.NOT,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -18:
						break;
					case 18:
						{/*reset pos to -1, if 0, otherwise line 1 starts at 0, rest start at 1 */ yychar=-1;}
					case -19:
						break;
					case 19:
						{ /* ignore white space. */ }
					case -20:
						break;
					case 20:
						{return new Symbol(sym.IF,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -21:
						break;
					case 21:
						{return new Symbol(sym.EQUAL,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -22:
						break;
					case 22:
						{return new Symbol(sym.AND,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -23:
						break;
					case 23:
						{/* ignore everything*/}
					case -24:
						break;
					case 24:
						{return new Symbol(sym.INT,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -25:
						break;
					case 25:
						{return new Symbol(sym.NEW,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -26:
						break;
					case 26:
						{return new Symbol(sym.MAIN,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -27:
						break;
					case 27:
						{return new Symbol(sym.BYTE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -28:
						break;
					case 28:
						{return new Symbol(sym.ELSE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -29:
						break;
					case 29:
						{return new Symbol(sym.TRUE,new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -30:
						break;
					case 30:
						{return new Symbol(sym.THIS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -31:
						break;
					case 31:
						{return new Symbol(sym.VOID,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -32:
						break;
					case 32:
						{/* ignore everything*/}
					case -33:
						break;
					case 33:
						{return new Symbol(sym.FALSE,new SymbolValue(yyline+1, yychar+1, yytext(), 0));}
					case -34:
						break;
					case 34:
						{return new Symbol(sym.WHILE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -35:
						break;
					case 35:
						{return new Symbol(sym.CLASS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -36:
						break;
					case 36:
						{return new Symbol(sym.IMPORT,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -37:
						break;
					case 37:
						{return new Symbol(sym.LENGTH,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -38:
						break;
					case 38:
						{return new Symbol(sym.STATIC,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -39:
						break;
					case 39:
						{return new Symbol(sym.RETURN,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -40:
						break;
					case 40:
						{return new Symbol(sym.PUBLIC,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -41:
						break;
					case 41:
						{return new Symbol(sym.STRING,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -42:
						break;
					case 42:
						{return new Symbol(sym.BOOLEAN,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -43:
						break;
					case 43:
						{return new Symbol(sym.EXTENDS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -44:
						break;
					case 44:
						{return new Symbol(sym.MEGGYTONE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -45:
						break;
					case 45:
						{return new Symbol(sym.MEGGY,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -46:
						break;
					case 46:
						{return new Symbol(sym.MEGGYDELAY,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -47:
						break;
					case 47:
						{return new Symbol(sym.MEGGYCOLOR,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -48:
						break;
					case 48:
						{return new Symbol(sym.MEGGYBUTTON,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -49:
						break;
					case 49:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 36363));}
					case -50:
						break;
					case 50:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 48541));}
					case -51:
						break;
					case 51:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 54485));}
					case -52:
						break;
					case 52:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 32397));}
					case -53:
						break;
					case 53:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 61157));}
					case -54:
						break;
					case 54:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 40816));}
					case -55:
						break;
					case 55:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 45816));}
					case -56:
						break;
					case 56:
						{return new Symbol(sym.MEGGYSETPIXEL,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -57:
						break;
					case 57:
						{return new Symbol(sym.MEGGYGETPIXEL,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -58:
						break;
					case 58:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 2));}
					case -59:
						break;
					case 59:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -60:
						break;
					case 60:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 34323));}
					case -61:
						break;
					case 61:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 51427));}
					case -62:
						break;
					case 62:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 57724));}
					case -63:
						break;
					case 63:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 38526));}
					case -64:
						break;
					case 64:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 43243));}
					case -65:
						break;
					case 65:
						{return new Symbol(sym.MEGGYTONESTART,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -66:
						break;
					case 66:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 4));}
					case -67:
						break;
					case 67:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -68:
						break;
					case 68:
						{return new Symbol(sym.MEGGYSETAUXLEDS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -69:
						break;
					case 69:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 0));}
					case -70:
						break;
					case 70:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 5));}
					case -71:
						break;
					case 71:
						{return new Symbol(sym.MEGGYCHECKBUTTON,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -72:
						break;
					case 72:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 16));}
					case -73:
						break;
					case 73:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 8));}
					case -74:
						break;
					case 74:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 4));}
					case -75:
						break;
					case 75:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 7));}
					case -76:
						break;
					case 76:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 32));}
					case -77:
						break;
					case 77:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 2));}
					case -78:
						break;
					case 78:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 3));}
					case -79:
						break;
					case 79:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 6));}
					case -80:
						break;
					case 81:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -81:
						break;
					case 82:
						{ System.err.println("Illegal character: "+yytext()); }
					case -82:
						break;
					case 83:
						{/*reset pos to -1, if 0, otherwise line 1 starts at 0, rest start at 1 */ yychar=-1;}
					case -83:
						break;
					case 85:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -84:
						break;
					case 86:
						{ System.err.println("Illegal character: "+yytext()); }
					case -85:
						break;
					case 88:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -86:
						break;
					case 90:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -87:
						break;
					case 92:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -88:
						break;
					case 94:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -89:
						break;
					case 96:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -90:
						break;
					case 98:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -91:
						break;
					case 100:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -92:
						break;
					case 102:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -93:
						break;
					case 104:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -94:
						break;
					case 106:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -95:
						break;
					case 108:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -96:
						break;
					case 110:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -97:
						break;
					case 112:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -98:
						break;
					case 114:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -99:
						break;
					case 116:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -100:
						break;
					case 118:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -101:
						break;
					case 120:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -102:
						break;
					case 122:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -103:
						break;
					case 124:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -104:
						break;
					case 126:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -105:
						break;
					case 198:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -106:
						break;
					case 216:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -107:
						break;
					case 223:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -108:
						break;
					case 225:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -109:
						break;
					case 226:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -110:
						break;
					case 227:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -111:
						break;
					case 228:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -112:
						break;
					case 229:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -113:
						break;
					case 230:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -114:
						break;
					case 231:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -115:
						break;
					case 232:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -116:
						break;
					case 233:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -117:
						break;
					case 234:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -118:
						break;
					case 235:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -119:
						break;
					case 236:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -120:
						break;
					case 237:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -121:
						break;
					case 238:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -122:
						break;
					case 239:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -123:
						break;
					case 240:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -124:
						break;
					case 241:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -125:
						break;
					case 242:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -126:
						break;
					case 243:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -127:
						break;
					case 244:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -128:
						break;
					case 245:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -129:
						break;
					case 246:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -130:
						break;
					case 247:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -131:
						break;
					case 248:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -132:
						break;
					case 249:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -133:
						break;
					case 250:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -134:
						break;
					case 251:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -135:
						break;
					case 252:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -136:
						break;
					case 253:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -137:
						break;
					case 254:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -138:
						break;
					case 255:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -139:
						break;
					case 256:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -140:
						break;
					case 257:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -141:
						break;
					case 258:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -142:
						break;
					case 259:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -143:
						break;
					case 262:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -144:
						break;
					case 263:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -145:
						break;
					case 264:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -146:
						break;
					case 265:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -147:
						break;
					case 266:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -148:
						break;
					case 267:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -149:
						break;
					case 268:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -150:
						break;
					case 269:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -151:
						break;
					case 270:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -152:
						break;
					case 271:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -153:
						break;
					case 272:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -154:
						break;
					case 273:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -155:
						break;
					case 274:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -156:
						break;
					case 275:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -157:
						break;
					case 276:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -158:
						break;
					case 277:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -159:
						break;
					case 278:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -160:
						break;
					case 279:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -161:
						break;
					case 280:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -162:
						break;
					case 281:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -163:
						break;
					case 282:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -164:
						break;
					case 284:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -165:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
