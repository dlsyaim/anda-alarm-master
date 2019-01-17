//test data
login(0x01);//ad cc 1b 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 34 e3 bc 7f 46 f3 98 15 d a
deviceEvent(0, new Date, 0X1384, 1);//ad cc e 0 2 0 2c 3a f 7 1 13 14 84 13 1 1 0 d a
deviceHeartbeat(1, 55, 50, 5, 100, 1);//ad cc b 0 7 1 37 32 0 5 0 64 0 1 0 d a
commandResult(0x06, 0x01);//ad cc 4 0 a 11 6 1 d a


function login(state, hostId) {
    var hostIdBytes = [21, 152, 243, 70, 127, 188, 227, 52].reverse();//little-endian
    return toHexSV(packet([0x01, 1, state].concat(zeros(16)).concat(hostIdBytes)));
}

function deviceEvent(state, time, eventType, defenceArea) {
    var timeBytes = [
        time.getSeconds(),
        time.getMinutes(),
        time.getHours(),
        time.getDate(),
        time.getMonth() + 1,
        time.getFullYear() % 100,
        Math.floor(time.getFullYear() / 100),
    ];
    return toHexSV(packet([0X02, state].concat(timeBytes)
        .concat(to2Bytes(eventType)).concat([1]).concat(to2Bytes(defenceArea))));
}

function deviceHeartbeat(state, a, b, c, d, e) {
    return toHexSV(packet(concat(0X07, state, a, to2Bytes(b), to2Bytes(c), to2Bytes(d), to2Bytes(e))));
}

function commandResult(cmdType, ret) {
    return toHexSV(packet(concat(0x0a, 0x11, cmdType, ret)));
}

function concat() {
    var ret = [];
    for (var i = 0; i < arguments.length; i++) {
        var e = arguments[i];
        ret = ret.concat(e instanceof Array ? e : [e]);
    }
    return ret;
}

function packet(dataBytes) {
    return [0xad, 0xcc].concat(to2Bytes(dataBytes.length)).concat(dataBytes).concat([0x0d, 0x0a]);
}

function to2Bytes(n) {
    return [(n & 0xFF), ((n & 0xFF00) >> 8)];//little-endian
}

function zeros(n) {
    var s = [];
    for (; n > 0; n--)
        s.push(0);
    return s;
}

function toHexSV(b) {
    return b.map(x => x.toString(16)).join(' ');
}