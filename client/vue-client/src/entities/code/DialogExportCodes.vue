<template>
    <div class="text-center">
        <v-dialog
            v-model="dialog"
            max-width="600px"
            persistent
            >
            <v-card >
                <v-card-title class="v-card__title_dialog" v-if="isButtonCreateCodes"> {{ $t('new_codes') }}
                <v-spacer></v-spacer>
                <v-btn icon  @click="close">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
                </v-card-title>
                <v-card-title class="v-card__title_dialog" v-if="!isButtonCreateCodes"> {{ $t('title_export_codes') }}
                <v-spacer></v-spacer>
                <v-btn icon  @click="close">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
                </v-card-title>
                
                <v-container>
                    <v-card-text v-if="isButtonCreateCodes" class="v-card__text_dialog"> {{ $t('create_new_codes') }}  </v-card-text>
                    <v-card-text v-if="!isButtonCreateCodes" class="v-card__text_dialog"> {{ $t('export_codes') }}  </v-card-text>
                    <v-card-text>
                        
                    <v-row>
                        <v-col >
                            <v-form ref="form" v-model="valid" lazy-validation>
                            <v-text-field v-if="isButtonCreateCodes"
                                v-model="codesNumber"
                                :rules="required_field($t('required_number_codes'))"
                                type="number"
                                required
                            ><template #label>
                                Número<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>

                            <v-text-field v-if="isButtonCreateCodes"
                                v-model="expirationDate"
                                :rules="required_field($t('required_expired_date'))"
                                name="expirationDate"
                                type="date"
                                required
                            ><template #label>
                                {{$t('expiration_date')}}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>

                            <v-radio-group v-model="exportFile" row mandatory v-if="isButtonCreateCodes">
                                <template #label>
                                Exportar<span class="red--text"><strong>* </strong></span>
                                </template>
                                <v-radio label="Sí" value="yes" color="#22555A"></v-radio>
                                <v-radio label="No" value="no" color="#22555A" ></v-radio>
                            </v-radio-group>

                            <v-text-field v-if="(exportFile == 'yes' || !isButtonCreateCodes)" 
                                v-model="fileName"
                                :rules="required_field($t('required_file_name'))"
                                name="fileName"
                                type="text"
                                required
                            ><template #label>
                                {{$t('file_name')}}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>

                            <v-radio-group v-model="formatFile" row mandatory v-if="(exportFile == 'yes' || !isButtonCreateCodes)">
                                <template #label>
                                {{$t('format_file')}}<span class="red--text"><strong>* </strong></span>
                                </template>
                                <v-radio label="CSV" value="csv" color="#22555A"></v-radio>
                                <v-radio label="EXCEL" value="excel" color="#22555A" ></v-radio>
                            </v-radio-group>

                            <v-row v-if="!isButtonCreateCodes">
                                <v-card-text v-if="!isButtonCreateCodes" class="v-card__text_dialog"> {{ $t('date_between') }}  </v-card-text>
                                <v-col cols="12" sm="6">
                                    <v-text-field id="creationDateInicio"
                                        v-model="creationDate1"
                                        :rules="validate_dates('creationDate1')"
                                        type="date"
                                        required
                                    ><template #label>
                                        {{$t('create_date_start')}}<span class="red--text"><strong>* </strong></span>
                                    </template></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6">
                                    <v-text-field id="creationDateFin"
                                        v-model="creationDate2"
                                        :rules="validate_dates('creationDate2')"
                                        type="date"
                                        required
                                    ><template #label>
                                        {{$t('create_date_end')}}<span class="red--text"><strong>* </strong></span>
                                    </template></v-text-field>
                                </v-col>
                            </v-row>
                            
                        </v-form>
                        </v-col>
                    </v-row>
                    </v-card-text>
                
                    <v-card-actions>
                        <v-btn
                            color="red"
                            text
                            @click="close"
                        >
                        {{$t('cancel')}}
                        </v-btn>
                        <v-spacer></v-spacer>
                        <v-btn v-if="isButtonCreateCodes"
                            color="green darken-1"
                            text
                            @click="create()"
                        >
                        {{$t('ok')}}
                        </v-btn>
                        <v-btn v-if="!isButtonCreateCodes"
                            color="green darken-1"
                            text
                            @click="exportCodesBetweenDates()"
                        >
                        {{$t('ok')}}
                        </v-btn>
                    </v-card-actions>
                </v-container>
            </v-card>  
        </v-dialog>
    </div>
</template>

<script>
import store from "../../common/store";
import RepositoryFactory from "@/repositories/RepositoryFactory";
const CodesRepository = RepositoryFactory.get("codes");
const BooksRepository = RepositoryFactory.get("books");
const XLSX = require('xlsx');

export default {
    props:{
        dialog: {
            type: Boolean,
            required: true
        },
        isButtonCreateCodes: {
            type: Boolean,
            required: true
        },
        bookId:{
            required: false
        },
        book:{
            type:Object,
            required: false
        },
    },
    data: () => ({
        valid:true,
        fileName:'',
        codesNumber: 100,
        expirationDate: '', 
        exportFile:'yes',
        formatFile:'csv',
        creationDate1: '',
        creationDate2: ''
    }),
    mounted(){
        //this.expirationDate = this.expDate
        if(new Date().getMonth() > 7){
            this.expirationDate = ''.concat(new Date().getFullYear()+1).concat('-08-31')
        }else{
            this.expirationDate = ''.concat(new Date().getFullYear()).concat('-08-31')
        }
        this.fileName = 'codigos_'.concat(this.book.title.replaceAll(' ','_').concat('_',this.book.course,'_',this.book.level).toLowerCase())
    },
    // watch:{
    //     fileName:{
    //             handler(val) {
                    
    //                 console.log(this.dialog)
    //                 // this.getFileName()
    //             },
    //             immediate: true,
    //         }
    // },
    methods: {
        validate () {
            this.$refs.form.validate()
        },
        resetValidation () {
            this.$refs.form.resetValidation()
        },
        required_field(msg){
            return [value => !!value || msg]
        },
        required_email(msg){
            return [ v => !!v || msg,
                        v => /.+@.+\..+/.test(v) || 'Email debe ser válido']
        },
        async getFileName(){
            this.book = await BooksRepository.getById(this.bookId)
            this.fileName = this.book.title.replaceAll(' ','_').concat('_',this.book.course,'_',this.book.level)
        },
        validate_dates(field){
            const t = this.$t.bind(this)
            if(this.creationDate1 <= this.creationDate2 && this.creationDate1 != '' && this.creationDate2!='') {
                this.$refs.form.inputs.forEach(input => {
                    if(input.id == 'creationDateFin') input.resetValidation()
                    if(input.id == 'creationDateInicio') input.resetValidation()
                })
            }
            if(field == 'creationDate1'){
                return [v => !!v || t('export_creation_date'), //v => v !== this.creationDate2 || 'Las fechas deben ser diferentes',
                            v => !(v > this.creationDate2 && this.creationDate2!='') || t('rules_creation_date_start')   
                        ]
            }
            if(field == 'creationDate2'){
                return [v => !!v || t('export_creation_date'), //v => v !== this.creationDate1 || 'Las fechas deben ser diferentes.',
                            v => !(v < this.creationDate1) || t('rules_creation_date_end')    
                        ]
            }
        },
        async exportCodesBetweenDates() {
            if (!this.$refs.form.validate()) {
                return
            }
            try {
                const codes = await CodesRepository.getByCreationDate(this.bookId,this.creationDate1, this.creationDate2)
                if(codes.length == 0){
                    const t = this.$t.bind(this)
                    this.$notify({
                        text: t('no_codes_in_dates'),
                        type: "warning"
                    })
                }else{
                    this.exportCodes(codes)
                }
                this.close()
            } catch (err) {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                })
            }
        },
        async create() {
                if (!this.$refs.form.validate()) {
                    return
                }
                try {
                    let formData = new FormData()
                    formData.append('number',this.codesNumber)
                    formData.append('expirationDate',this.expirationDate)
                    formData.append('userId',store.state.user.id)
                    
                    const codes = await CodesRepository.create(this.bookId,formData)
                    if(this.exportFile == 'yes' ){
                        this.exportCodes(codes)
                    }
                    
                    this.$emit('close-dialog',false)
                    this.close()
                    this.$router.go({
                        name: "CodeList"
                    })

                } catch (err) {
                    this.$notify({
                        text: err.response.data.message,
                        type: "error"
                    })
                }
        },
        exportToExcel(data){ 
            const workSheetColumnNames = [['Codigo','URL']]
            const workBook = XLSX.utils.book_new()
            const workSheet = XLSX.utils.json_to_sheet(data,{origin:'A2',skipHeader:true})
            XLSX.utils.sheet_add_aoa(workSheet, workSheetColumnNames)
            XLSX.utils.book_append_sheet(workBook,workSheet,this.fileName)
            XLSX.writeFile(workBook,this.fileName.concat('.xlsx'))
        },
        csvmaker(data) {
            const csvRows = []
            const headers = ['Codigo','URL']
            csvRows.push(headers.join(','))
            // // Pushing Object values into array  with comma separation
            for(var i=0;i<data.length;i++){
                const values = Object.values(data[i]).join(',')
                csvRows.push(values)
            }
            // // Returning the array joining with new line
            return csvRows.join('\n')
        },
        download(data){
            const blob = new Blob([data], { type: 'text/csvcharset=utf-8' })
            // Creating an object for downloading url
            const url = window.URL.createObjectURL(blob)
            // Creating an anchor(a) tag of HTML
            const a = document.createElement('a')
            // Passing the blob downloading url
            a.setAttribute('href', url)
            // Setting the anchor tag attribute for downloading and passing the download file name
            a.setAttribute('download', this.fileName.concat('.csv'))
            // Performing a download with click
            a.click()
        },
        exportCodes(codes){
            const codes_ = new Array()
            for(var i = 0; i < codes.length ;i++){
                const cds = new Object()
                cds.code = codes[i].code
                cds.url = 'http://localhost:1234/#/books/'+codes[i].code
                codes_.push(cds)
            }
            codes_.join("\n")
            // console.log(codes_)
            if(this.formatFile == 'csv'){
                const csvdata = this.csvmaker(codes_)
                this.download(csvdata)
            }else{
                this.exportToExcel(codes_)
            }
            this.close()
            
            
        },
        
        close(){
            this.$emit('close-dialog',false)       
            if(new Date().getMonth() > 7){
                this.expirationDate = ''.concat(new Date().getFullYear()+1).concat('-08-31')
            }else{
                this.expirationDate = ''.concat(new Date().getFullYear()).concat('-08-31')
            }
            this.resetValidation()
            this.formatFile='csv'
            this.codesNumber = 100
            //this.fileName = this.book_.title.replaceAll(' ','_').concat('_',this.book.course,'_',this.book.level)
            this.exportFile = 'yes'
            this.creationDate1 = ''
            this.creationDate2 = ''
        }
    }
}
</script>

<style scoped>

.v-card__title_dialog {
    background-color:#DDEEEB;
    font-weight: bold;
    justify-content: start
}
.v-card__text_dialog {
    font-weight: bold;
    text-align: start;
}

</style>
    