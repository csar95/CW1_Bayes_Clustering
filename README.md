# F21DL_Coursework1

a = sorted([(int(round(x/48)), x % 48) for x in bottom_2_array])
a = a.filter(lambda (x, y): x >10 or x < 37)
print(a)
b = sorted([x % 48 for x in bottom_5_array])
print(b)
c = sorted([(int(round(x[0]/48)), x[0] % 48, x[1]) for x in top_10_array])
print(c)
