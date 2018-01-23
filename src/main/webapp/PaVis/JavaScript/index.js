let opacitySpeed = 0.95;

let hidePathLevel = 3/opacitySpeed;

let showGeneLevel = 4/opacitySpeed;
let hideGeneLevel = 6/opacitySpeed;

let showSequenceLevel = 1/opacitySpeed;
let hideSequenceLevel = 10/opacitySpeed;

let changed = false;

let serverDN = "http://localhost:8080/"

function ajaxGet(url, callback) {
    console.log("Requesting AJAX");
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            console.log("AJAX ready");
            callback(this.responseText)
        }
    };
    xhttp.open("GET", url, true);
    xhttp.send();
}

//TODO Test if working
function ajaxPost(url, data,callback){
    console.log("Requesting AJAX");
    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            console.log("AJAX ready");
            callback(this.responseText)
        }
    };
    xhttp.open("POST", url, true);
    xhttp.send(data);

}

function startPage(){
    ajaxGet('http://localhost:8080/rest/Gene/Pangenome?genome=3&genome=4',startVisualisation)
    //startVisualisation()
}

function startVisualisation(ajaxOutput) {
    let json = JSON.parse(ajaxOutput)

    let nodes = json[1];
    let paths = json[0];

    console.log(nodes)
    console.log(paths)

    let svg = d3.select('#svg')

    /*
    const nodes = [
        {name: 'Test', seq: 'AAAA', gene: null},
        {name: 'B', seq: 'BBB', gene: "Big"},
        {name: 'C', seq: 'CCC', gene: null},
        {name: 'D', seq: 'DDD', gene: "Door"},
        {name: 'E', seq: 'EEE', gene: "Extra"},
    ];

    const paths = [
        {id: 0, name: 'Genome A', sequence: ['Test', 'C', 'B', 'E']},
        {id: 1, name: 'Genome B', sequence: ['Test', 'B', 'C', 'E']},
        {id: 2, name: 'Genome C', sequence: ['Test', 'D', 'C', 'E']},
    ];
*/

    createPaVis();

    function createPaVis() {
        create({
            svgID: '#svg',
            nodes,
            tracks: paths,
        });
        useColorScheme(0)
        //drawGeneLine()

        let zoom = getZoom(0, 20)
        svg.call(zoom).on('dblclick.zoom', null)
        d3.selectAll(".checkBox").on('change', graphReset);
    }

    function graphReset() {
        let zoom = getZoom(0, 20)
        svg.select('g#PaVis').attr('transform', 'translate(0,0) scale(1)');
        svg.call(zoom).on('dblclick.zoom', null)
        changed = true;
        //drawGeneLine()
    }

    function getZoom(minScale, maxScale) {
        var zoom = d3.zoom()
            .scaleExtent([minScale, maxScale])
            .on('zoom', zoomed)
        return zoom
    }

    function zoomed() {
        let transform = d3.event.transform;
        transformation(transform);
        showAndHide(transform);

        function showAndHide(transform) {
            let g = svg.select('g');

            let allGene = g.selectAll('.gene');
            let allPolyLines = g.selectAll('polyline')
            let allSequence = g.selectAll('.sequence');

            let allPath = svg.selectAll('path');
            let allRect = svg.selectAll('rect');

            let hidePath = 1 - (transform.k - hidePathLevel) * opacitySpeed < 0.1;

            if (hidePathLevel < transform.k && !hidePath) {
                allPath.style('opacity', 1 - (transform.k - hidePathLevel) * opacitySpeed);
                allRect.style('opacity', 1 - (transform.k - hidePathLevel) * opacitySpeed);
            }
            else if (hidePath) {
                allPath.style('opacity', 0.1);
                allRect.style('opacity', 0.1);
            }
            else {
                allPath.style('opacity', 1);
                allRect.style('opacity', 1);
            }

            if (showGeneLevel <= transform.k && transform.k <= hideGeneLevel) {
                allGene.style('opacity', 1);
                allPolyLines.style('opacity', 1)
            }
            else if (showGeneLevel > transform.k) {
                allGene.style('opacity', 1 - (showGeneLevel - transform.k) * opacitySpeed);
                allPolyLines.style('opacity', 1 - (showGeneLevel - transform.k) * opacitySpeed);

            }
            else if (hideGeneLevel < transform.k) {
                allGene.style('opacity', 1 - (transform.k - hideGeneLevel) * opacitySpeed);
                allPolyLines.style('opacity', 1 - (transform.k - hideGeneLevel) * opacitySpeed);
            }

            if (showSequenceLevel <= transform.k && transform.k <= hideSequenceLevel) {
                allSequence.style('opacity', 1);
            }
            else if (showSequenceLevel > transform.k) {
                allSequence.style('opacity', 1 - (showSequenceLevel - transform.k) * opacitySpeed);
            }
            else if (hideSequenceLevel < transform.k) {
                allSequence.style('opacity', 1 - (transform.k - hideSequenceLevel) * opacitySpeed);
            }
        }
    }

    function transformation(transform) {
        if (changed === true) {
            transform.y = 0
            transform.x = 0
            changed = false
        }


        let transformX = transform.x;
        let transformY = transform.y;

        svg.select('g#PaVis').attr("transform",
            "translate(" + transformX + "," +
            "" + transformY + ") " +
            "scale(" + transform.k +
            ")");
    }

    d3.selection.prototype.moveToBack = function () {
        return this.each(function () {
            var fChild = this.parentNode.firstChild;
            if (fChild) {
                this.parentNode.insertBefore(this, fChild)
            }
        })
    }

    d3.selection.prototype.moveToFront = function () {
        return this.each(function () {
            this.parentNode.appendChild(this)
        })
    }

    svg.select('g').selectAll('.gene').moveToFront()
    svg.select('g').selectAll('text').moveToFront()
    svg.select('g').selectAll('.gene').on('click', function(d){
        let url = "http://localhost:8080/rest/Kmers/Gene?";
        url += "gene="+d.name;

        for(let i=0;paths.length>i;i++){
            let path = paths[i];
            if(path.sequence.indexOf(d.name) > -1){
                url += "&genome="+path.genomeNumber
            }
        }

        console.log(url);

        ajaxGet(url, showGene)
    });

    function showGene(kmers) {
        console.log(kmers)
    }
}


$(document).ready(startPage());

